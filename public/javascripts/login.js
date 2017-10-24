/* Mostrador de caixa de erro */
var mostrador = {
  doms: {warnings: document.getElementById("warnings"), loginbox: document.getElementById("inner_login")},
  message: function(m){
    if(typeof m === "string") mostrador.doms.warnings.innerText = m
    mostrador.movimentar()
  },
  limpar: function(){mostrador.doms.warnings.innerText = ""},
  movimentar: function(){
    mostrador.doms.loginbox.style.animation = "error 0.25s ease"
    setTimeout(function(){mostrador.doms.loginbox.style.animation = ""}, 250)
  }
}

/* Verifica o formato da matrícula */
document.getElementById("matricula_input").addEventListener("change", function(e){
  var d = e.target.value
  if(d.split("")[2] != "/") mostrador.message("Formato inválido")
  else if(d.length != 10) mostrador.message("Tamanho inválido")
  else if(d.match(/[0-9\/]/g).length != d.split("").length) mostrador.message("Caracteres inválidos")
  else mostrador.limpar()
})

/* Detecta o tamanho da tela */
function screenSelect() {
	var w = screen.width * (window.devicePixelRatio || 1);
	var il = document.getElementById("inner_login");
	var cronograma = document.getElementById("cronograma");
	
	if (w > 1200) {
	  cronograma.style.display = ""
	  il.style.width = ""
	}
	else {
	  cronograma.style.display = "none"
	  il.style.width = "80%"
	}
}

setInterval(screenSelect, 100);
