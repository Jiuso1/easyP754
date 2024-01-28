let systemForm = document.getElementById("systemForm");

systemForm.addEventListener("submit", function (e) {
  e.preventDefault(); //No va a ningún action, se queda en la web.
  var formData = new FormData(systemForm); //Recogemos los datos del formulario en formData.

  //Si los datos introducidos no son válidos, avisamos al usuario:
  if (!dataIsOk(formData)) {
    alert("Error: compruebe los datos introducidos.");
    return;
  }

  //Prueba recorriendo el Object formData con el iterador entries(). Usar typeof para comprobar tipos.
  /*for (let pair of formData.entries()) {
    console.log(pair[0] + ": " + pair[1]);
  }*/

  //Recogemos en variables los datos del formulario guardados en formData:
  let precisionMode = formData.get("precisionMode");
  let number = formData.get("number");
  let sign = 0;
  let exponent = 0;
  let mantissa = 0;
  let excess = precisionMode.localeCompare("simplePrecision") == 0 ? 127 : 1023; //Si el modo elegido es simple precisión, el exceso es 127. Sino (si es doble precisión), el exceso es 1023.

  //Escribimos lo que vamos a añadir al HTML:
  let html =
    "<h2>Resultado</h2>" +
    "<h4>Introducción</h4>" +
    '<img src="./areas.png"></img>' +
    "<p>Para representar un número en el estándar IEEE P754 debemos saber si este pertenece a la zona normalizada o a la zona desnormalizada.</p>" +
    "<p>En función de la zona a la que pertenezca dicho número, emplearemos una fórmula u otra.</p>" +
    "<p>El exceso en simple precisión vale 127, y en doble precisión vale 1023.</p>" +
    "<p>En la imagen anterior podemos apreciar estas zonas tanto en el rango negativo como en el positivo, con sus respectivas fórmulas:</p>" +
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
    "<dd>Emplea 23 bits en el caso de simple precisión, y 52 bits en el caso de doblep precisión.</dd>" +
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
      " es positivo, compararemos con el punto azul de la zona desnormalizada positiva. Es decir, con el número más pequeño de esa zona.</p>" +
      "<p>El punto azul que estamos estudiando es positivo, por tanto el bit de signo (S) vale 0.</p>" +
      "<p>Como este punto se encuentra en la zona normalizada, el campo exponente (E) debe ser distinto de 0 (observar imagen de Introducción). " +
      "Como es el valor más pequeño, pondremos el campo exponente más pequeño que podamos (1)</p>" +
      "<p>La zona normalizada no impone restricciones de ningún tipo al campo mantisa (M), así que pondremos la M más pequeña que podamos (0).</p>" +
      "<p>Ya tenemos S, E y M, así que basta utilizar la fórmula de la zona normalizada y operar.</p>" +
      '<div class="equation">' +
      "<p>V(X) = (-1)<sup>S</sup> &times 1,M &times 2<sup>E-EXCESO</sup></p>" +
      "<p>V(X) = (-1)<sup>" +
      sign +
      "</sup> &times 1," +
      mantissa +
      " &times 2<sup>" +
      exponent +
      "-" +
      excess +
      "</sup></p>" +
      "</div>";
  } else {
    console.log("Numero negativo");
  }
  document.body.innerHTML = document.body.innerHTML + html; //Añadimos al HTML todo lo escrito.
});

//Devuelve true si los datos introducidos son correctos, false en caso contrario.
function dataIsOk(formData) {
  /*console.log(
    "El numero introducido es " +
      formData.get("number") +
      " y la comparacion sale " +
      isNaN(formData.get("number"))
  );*/
  //Si no hay seleccionado ningún modo de precisión o si el valor introducido no es un número (en tal caso el casting a Number dará NaN, Not a Number):
  if (!formData.has("precisionMode") || isNaN(formData.get("number"))) {
    return false; //Los datos introducidos no son correctos.
  } else {
    return true; //Los datos introducidos sí son correctos.
  }
}
