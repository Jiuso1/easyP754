//console.log((0.2 * 10 + 0.1 * 10) / 10);
//console.log(numberOfDecimals(3.569329));
//console.log((0.2 * 10 + 0.1 * 10) / 10);
//console.log("div: " + div(7, 2));

let systemForm = document.getElementById("systemForm");

systemForm.addEventListener("submit", function (e) {
  let number = null; //Inicializamos a null el número.

  e.preventDefault(); //No va a ningún action, se queda en la web.
  var formData = new FormData(systemForm); //Recogemos los datos del formulario en formData.

  //Si los datos introducidos no son válidos, avisamos al usuario:
  if (!dataIsOk(formData)) {
    alert("Error: compruebe los datos introducidos.");
    return;
  }

  if (formData.get("number").indexOf("**") != -1) {
    //Si contiene el operador exponente:
    number = eval(formData.get("number")); //Evaluamos y realizamos las operaciones aritméticas pertinentes.
    //console.log("Evalúo");
  } else {
    number = formData.get("number"); //No evaluamos ninguna expresión, simplemente transformamos la String a Number.
    //console.log("Transformo");
  }

  //Recogemos en variables los datos del formulario guardados en formData:
  const precisionMode = formData.get("precisionMode");
  let sign = 0;
  let exponent = 0;
  let mantissa = 0;
  const excess =
    precisionMode.localeCompare("simplePrecision") == 0 ? 127 : 1023; //Si el modo elegido es simple precisión, el exceso es 127. Sino (si es doble precisión), el exceso es 1023.
  //Variables necesarias para los cálculos:
  let y = 0;
  let x = 0;
  let roundedY = 0;
  let previousMultiplicationOperand = new Decimal(0);
  let nextMultiplicationOperand = new Decimal(0);
  let operationsCounter = 0; //Cuando se realiza una operación en el bucle while encargado de calcular la mantisa (M), incrementa en uno.
  let mantissaBit = 0;
  //Variables que controlan la presentación del resultado:
  const mantissaNumberBits =
    precisionMode.localeCompare("simplePrecision") == 0 ? 23 : 52; //Si el modo elegido es simple precisión, el nº de bits de la mantisa es 23. Sino (si es doble precisión), es 52.
  const exponentNumberBits =
    precisionMode.localeCompare("simplePrecision") == 0 ? 8 : 11; //Si el modo elegido es simple precisión, el nº de bits del exponente es 8. Sino (si es doble precisión), es 11.
  let mantissaArray = Array.from({ length: mantissaNumberBits }, () => 0); //Creamos un array de bits para la mantisa, inicializado a 0 por defecto.
  let exponentArray = null;

  //Escribimos lo que vamos a añadir al HTML:
  let html =
    "<h2>Resultado</h2>" +
    '<h4><a name="introduccion">Introducción</a></h4>' +
    '<img src="./areas.png"></img>' +
    "<p>Para representar un número en el estándar IEEE P754 debemos saber si este pertenece a la zona normalizada o a la zona desnormalizada.</p>" +
    "<p>En función de la zona a la que pertenezca dicho número, emplearemos una fórmula u otra.</p>" +
    "<p>El exceso en simple precisión vale 127, y en doble precisión vale 1023.</p>" +
    "<p>En la imagen anterior podemos apreciar estas zonas tanto en el rango negativo como en el positivo, con sus respectivas fórmulas.</p>" +
    "<p>Vemos un espacio entre las distintas zonas. Esto es debido a las limitaciones físicas y teóricas del sistema de representación, no podemos representar" +
    " todos los números que existen. También vemos que el número 0, el infinito y las indeterminaciones son casos aparte de este sistema. Si ingresa alguno de estos " +
    "casos en la calculadora, podrá ver cómo se representan estos casos especiales.</p>" +
    "<h4>Formato de la representación</h4>" +
    '<img src="./format.png"></img>' +
    "<p>¿Qué son S,  E y M? Son términos de este estándar, ¡vamos a detallarlos!</p>" +
    '<div class="listContainer">' +
    '<dl class="myUL">' +
    "<dt>S (bit de signo)</dt>" +
    "<dd>Indica si el número representado es positivo o negativo. Cuando vale 0 el número representado es positivo (+), y cuando vale 1 el número representado es negativo (-).</dd>" +
    "<dt>E (campo exponente)</dt>" +
    "<dd>Emplea 8 bits en el caso de simple precisión, y 11 bits en el caso de doble precisión.</dd>" +
    "<dt>M (campo mantisa)</dt>" +
    "<dd>Emplea 23 bits en el caso de simple precisión, y 52 bits en el caso de doble precisión.</dd>" +
    "</dl>" +
    "</div>" +
    "<p>La combinación de estos tres términos representa un número, y nosotros vamos a calcular estos para representar el número decimal dado.</p>" +
    "<p>Es decir, seremos capaces de escribir todos los 0 y 1 que representan el número decimal dado, usando IEEE P754.</p>" +
    "<h4>¿A qué zona pertenece el número?</h4>" +
    '<img src="./points.png"></img>' +
    "<p>Como " +
    number +
    " no es un caso especial (no es +0, -0, +∞, -∞ o alguna indeterminación), este valor seguro que se encuentra o en la zona normalizada " +
    "o en la zona desnormalizada.</p>" +
    "<p>El punto azul izquierdo representa el valor más pequeño posible de la zona desnormalizada negativa, y el punto azul derecho representa " +
    "el valor más pequeño posible de la zona normalizada positiva.</p> " +
    "<p>Basta comparar con el punto azul de su mismo signo, y si es mayor o menor a este podremos decir que se encuentra en " +
    "una zona u otra.</p>";
  if (number > 0) {
    sign = 0; //Es positivo, por tanto S = 0.
    exponent = 1; //E = 1.
    mantissa = 0; //M = 0.
    html +=
      "<p>" +
      number +
      " es positivo, compararemos con el punto azul de la zona normalizada positiva. Es decir, con el número más pequeño de esa zona.</p>" +
      "<p>El punto azul que estamos estudiando (el derecho) es positivo, por tanto el bit de signo (S) vale 0.</p>" +
      '<p>Como este punto se encuentra en la zona normalizada, el campo exponente (E) debe ser distinto de 0 (observar imagen de <a href="#introduccion">Introducción</a>). ' +
      "Como es el valor más pequeño, pondremos el campo exponente más pequeño que podamos (1)</p>" +
      "<p>La zona normalizada no impone restricciones de ningún tipo al campo mantisa (M), así que pondremos la M más pequeña que podamos (0).</p>" +
      "<p>Ya tenemos S, E y M, así que basta con utilizar la fórmula de la zona normalizada y operar.</p>" +
      '<div class="equation">' +
      "<p>V(X) = (-1)<sup>S</sup> &times; 1,M &times; 2<sup>E-EXCESO</sup></p>" +
      "</div>" +
      '<div class="equation">' +
      "<p>V(X) = (-1)<sup>" +
      sign +
      "</sup> &times; 1," +
      mantissa +
      " &times; 2<sup>" +
      exponent +
      "-" +
      excess +
      "</sup></p>" +
      "</div>" +
      '<div class="equation">' +
      "<p>V(X) = 2<sup>" +
      (exponent - excess) +
      "</sup></p>" +
      "</div>";
    if (number >= Math.pow(2, exponent - excess)) {
      //Si el número introducido por teclado es mayor que el punto azul, este se encuentra en la zona normalizada.
      //Realizamos los cálculos pertinentes para mostrarlos más adelante:
      y = Math.log2(number);
      roundedY = Math.floor(y); //Redondeamos la Y' al entero menor más próximo posible.
      x = number / 2 ** roundedY;

      html +=
        "<p>Como " +
        number +
        " es mayor o igual que el punto azul (" +
        "2<sup>" +
        (exponent - excess) +
        "</sup>), se encuentra en la zona normalizada.</p>";

      exponent = roundedY + excess;
      previousMultiplicationOperand = decimals(x); //Solo nos quedamos con la parte decimal de la x. Falla decimals, POR REVISAR.
      nextMultiplicationOperand = new Decimal(0);
      exponentArray = exponent.toString(2); //exponentArray almacena exponent en forma binaria, en una cadena de texto.

      html +=
        "<h4>Cálculos</h4>" +
        "<p>Sabiendo que " +
        number +
        " pertenece a la zona normalizada, podemos emplear su fórmula.</p>" +
        '<div class="equation">' +
        "<p>V(X) = " +
        number +
        " = 1,M &times; 2<sup>E-" +
        excess +
        "</sup></p>" +
        "</div>" +
        '<p>Consideraremos el siguiente sistema de tres ecuaciones con incógnitas <span class="bold">X</span> e <span class="bold">Y</span>.</p>' +
        '<div class="equation">' +
        "<p>V(X) = " +
        number +
        ' = <span class="blueXSquare">1,M<span class="x_item">x</span></span> &times; 2<sup><span class="redYSquare">E-' +
        excess +
        '<span class="y_item">y</span></span></sup></p>' +
        "</div>" +
        '<div class="equation">' +
        "<p>y &isin; &integers;</p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>1 &le; x &lt; 2</p>" +
        "</div>" +
        '<p>Supongo <span class="bold">X</span> = 1,0. La única incógnita será <span class="bold">Y\'</span>.</p>' +
        '<div class="equation">' +
        "<p>" +
        number +
        " = 1,0 &times; 2<sup>y'</sup></p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>" +
        number +
        " = 2<sup>y'</sup></p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>log<sub>2</sub>(" +
        number +
        ") = y'</p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>y' = " +
        y +
        "</p>" +
        "</div>" +
        "<p>Redondeamos al entero menor más próximo.</p>" +
        '<div class="equation">' +
        "<p> y = " +
        roundedY +
        "</p>" +
        "</div>" +
        '<p>Sabiendo <span class="bold">Y</span> podemos despejar el exponente (E).</p>' +
        '<div class="equation">' +
        "<p>y = E - " +
        excess +
        "</p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>" +
        roundedY +
        " = E - " +
        excess +
        "</p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>E = " +
        exponent +
        "</p>" +
        "</div>" +
        '<p>Sustituimos <span class="bold">Y</span> en la ecuación con recuadros de colores, dejando <span class="bold">X</span> como incógnita.</p>' +
        '<div class="equation">' +
        "<p>V(X) = " +
        number +
        " = x &times; 2<sup>" +
        roundedY +
        "</sup></p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>x = " +
        number +
        " &divide; " +
        2 ** roundedY +
        "</p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>x = " +
        x +
        "</p>" +
        "</div>" +
        '<p>Nos falta hallar la mantisa (M). Esta se calcula cogiendo la parte decimal de la <span class="bold">X</span>, ' +
        "multiplicándola por dos hasta llegar a 1,00. Los bits, que van de mayor a menor peso, son coloreados.</p>";
      //Mientras el resultado no sea 1,00 y mientras no hayamos pasado el nº de operaciones máximo:
      while (
        nextMultiplicationOperand != 1 &&
        operationsCounter < mantissaNumberBits
      ) {
        nextMultiplicationOperand = Decimal.mul(
          previousMultiplicationOperand,
          2
        ); //Multiplicamos por dos el operando previo.
        mantissaBit = div(nextMultiplicationOperand, 1); //Realizamos una división entera de 1 al nextMultiplicationOperand. nextMultiplicationOperando pertenece a [0,2).
        html +=
          '<div class="equation"><p>' +
          previousMultiplicationOperand +
          " &times; 2 = " +
          '<span class="orange">' +
          mantissaBit +
          "</span>" +
          "." +
          decimalsToString(decimals(nextMultiplicationOperand)) + //Separamos parte decimal y parte binaria para colorear los decimales.
          "</p></div>";
        previousMultiplicationOperand = nextMultiplicationOperand;
        if (previousMultiplicationOperand >= 1) {
          previousMultiplicationOperand = Decimal.sub(
            previousMultiplicationOperand,
            1
          );
          mantissaArray[mantissaNumberBits - operationsCounter - 1] = 1;
        }
        operationsCounter++;
      }
    } else {
      x = number / 2 ** (-excess + 1);
      exponent = 0; //En la zona desnormalizada, E = 0.
      previousMultiplicationOperand = decimals(x); //Solo nos quedamos con la parte decimal de la X.
      nextMultiplicationOperand = new Decimal(0);
      exponentArray = exponent.toString(2); //exponentArray almacena exponent en forma binaria, en una cadena de texto.

      console.log("Numero menor al punto azul positivo");
      html +=
        "<p>Como " +
        number +
        " es menor que el punto azul (" +
        "2<sup>" +
        (exponent - excess) +
        "</sup>), se encuentra en la zona desnormalizada.</p>";

      html +=
        "<h4>Cálculos</h4>" +
        "<p>Sabiendo que " +
        number +
        " pertenece a la zona desnormalizada, podemos emplear su fórmula.</p>" +
        '<div class="equation">' +
        "<p>V(X) = " +
        number +
        " = 0,M &times; 2<sup>-" +
        excess +
        " + 1" +
        "</sup></p>" +
        "</div>" +
        "<p>Desarrollamos el exponente.</p>" +
        '<div class="equation">' +
        "<p>V(X) = " +
        number +
        " = 0,M &times; 2<sup>" +
        (-excess + 1) +
        "</sup></p>" +
        "</div>" +
        '<p>Consideraremos el siguiente sistema de dos ecuaciones con incógnita <span class="bold">X</span>.</p>' +
        '<div class="equation">' +
        "<p>V(X) = " +
        number +
        ' = <span class="blueXSquare">0,M<span class="x_item">x</span></span> &times; 2<sup>  ' +
        (-excess + 1) +
        "</sup></p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>0 &le; x &lt; 1</p>" +
        "</div>" +
        '<p>Resolvemos la ecuación que tiene como incógnita <span class="bold">X</span>.' +
        '<div class="equation">' +
        "<p>V(X) = " +
        number +
        " = x &times; 2<sup>" +
        (-excess + 1) +
        "</sup></p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>x = " +
        number +
        " &divide; " +
        2 ** (-excess + 1) +
        "</p>" +
        "</div>" +
        '<div class="equation">' +
        "<p>x = " +
        x +
        "</p>" +
        "</div>" +
        '<p>Nos falta hallar la mantisa (M). Esta se calcula cogiendo la parte decimal de la <span class="bold">X</span>, ' +
        "multiplicándola por dos hasta llegar a 1,00. Los bits, que van de mayor a menor peso, son coloreados.</p>";
    }
    //Mientras el resultado no sea 1,00 y mientras no hayamos pasado el nº de operaciones máximo:
    while (
      nextMultiplicationOperand != 1 &&
      operationsCounter < mantissaNumberBits
    ) {
      nextMultiplicationOperand = Decimal.mul(previousMultiplicationOperand, 2); //Margen de error a corregir, PENDIENTE.
      mantissaBit = div(nextMultiplicationOperand, 1);
      html +=
        '<div class="equation"><p>' +
        previousMultiplicationOperand +
        " &times; 2 = " +
        '<span class="orange">' +
        mantissaBit +
        "</span>" +
        "." +
        decimalsToString(decimals(nextMultiplicationOperand)) + //Separamos parte decimal y parte binaria para colorear los decimales. + //Separamos parte decimal y parte binaria para colorear los decimales.
        "</p></div>";
      previousMultiplicationOperand = nextMultiplicationOperand; //Aquí es donde hay problemas, REVISAR.
      if (previousMultiplicationOperand >= 1) {
        previousMultiplicationOperand = Decimal.sub(
          previousMultiplicationOperand,
          1
        );
        mantissaArray[mantissaNumberBits - operationsCounter - 1] = 1;
      }
      operationsCounter++;
    }

    //Por último, mostramos el resultado recorriendo los arrays de bits que hemos calculado anteriormente:
    html += "<h4>Resultado</h4>" + "<table><tr><th>S</th>";

    //Hacemos una "extensión de signo". Necesitamos forzosamente exponentNumberBits, así que rellenamos con 0s si nos faltan bits.
    while (exponentArray.length < exponentNumberBits) {
      exponentArray = "0" + exponentArray;
    }

    //Dibujamos los headers del resultado:
    for (i = exponentNumberBits - 1; i >= 0; i--) {
      html += "<th>E<sub>" + i + "</sub></th>";
    }

    for (i = -1; i > -mantissaNumberBits - 1; i--) {
      html += "<th>M<sub>" + i + "</sub></th>";
    }

    html += "</tr>" + "<tr><th>" + sign + "</th>";
    for (i = 0; i < exponentNumberBits; i++) {
      html += "<th>" + exponentArray.charAt(i) + "</th>";
    }
    for (i = mantissaNumberBits - 1; i >= 0; i--) {
      html += "<th>" + mantissaArray[i] + "</th>";
    }
    html += "</tr>" + "</table>";
  } else {
    console.log("Numero negativo");
  }
  document.body.innerHTML = document.body.innerHTML + html; //Añadimos al HTML todo lo escrito.
});

//Devuelve true si los datos introducidos son correctos, false en caso contrario.
function dataIsOk(formData) {
  //Si no hay seleccionado ningún modo de precisión o si el valor introducido no es un número (en tal caso el casting a Number dará NaN, Not a Number):
  if (
    !formData.has("precisionMode") ||
    isNaN(formData.get("number") && formData.get("number").indexOf("**") == -1)
  ) {
    return false; //Los datos introducidos no son correctos.
  } else {
    return true; //Los datos introducidos sí son correctos.
  }
}

//Realiza una división entera. Cortesía de https://stackoverflow.com/questions/4228356/how-to-perform-an-integer-division-and-separately-get-the-remainder-in-javascr.
function div(x, y) {
  return Decimal.trunc(Decimal.div(x, y));
}

//Devuelve un Decimal con los decimales del parámetro x.
function decimals(x) {
  let stringDecimals = "";
  let stringNumber = x.toString();

  stringDecimals = stringNumber.split(".")[1]; //Para entender esto, consultar https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/split.
  if (stringDecimals === undefined) {
    //Si no tiene decimales:
    stringDecimals = "0";
  } else {
    //Si sí tiene decimales:
    stringDecimals = "0." + stringDecimals; //Le concatenamos '0.', para convertirlo a Decimal posteriormente.
  }

  return Decimal(stringDecimals); //Convertimos la String a Decimal y lo retornamos.
}

//Devuelve un entero: el número de decimales de un Number pasado por el parámetro x.
function numberOfDecimals(x) {
  let stringNumber = x.toString(); //Convertimos el Number recibido a String.
  return stringNumber.split(".")[1].length; //Devuelve el número de caracteres de la cadena de después del '.'.
}

//Devuelve una string: los decimales almacenados en el decimal pasado por parámetro.
function decimalsToString(decimals) {
  let stringDecimals = decimals.toString().split(".")[1]; //Convertimos el Decimal con decimales recibido a String.
  if (stringDecimals === undefined) {
    //Si no había decimales (por ejemplo, al recibir un Decimal que después del punto tiene 0. Ejemplos: 1.0, 32.0...):
    stringDecimals = "0";
  }
  return stringDecimals;
}
º;
