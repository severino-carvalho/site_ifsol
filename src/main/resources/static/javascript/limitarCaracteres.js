function limitarCaracteres() {
    let textos = document.getElementsByName("texto_noticia");

    Array.prototype.map.call(textos, (texto) => {
        texto.innerText = texto.innerText.slice(0, 300).trim() + "...";
    });
}

window.addEventListener("load", limitarCaracteres);