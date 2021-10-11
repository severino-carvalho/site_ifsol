
function limitarCaracteres() {
    let url = window.location.pathname;
    let textos = document.getElementsByName("texto_noticia");
    let qtdLimite = 0;

    if (url != "/publico/noticia/23") {
        qtdLimite = 300;
    } else {
        qtdLimite = 150;
    }
    
    Array.prototype.map.call(textos, (texto) => {
        texto.innerText = texto.innerText.slice(0, qtdLimite).trim() + "...";
    });
}

window.addEventListener("load", limitarCaracteres);