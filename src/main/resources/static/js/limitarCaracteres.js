
function limitarCaracteres(limit) {
    let textos = document.getElementsByName("texto_noticia");

    if (!limit) {
        limit = 300;
    }
    
    Array.prototype.map.call(textos, (texto) => {
        texto.innerText = texto.innerText.slice(0, limit).trim() + "...";
    });
}