/* Mostrador de caixa de erro */
var mostrador = {
  doms: {
    warnings: document.getElementById("warnings"), 
    loginbox: document.getElementById("inner_login"),
    button: document.getElementById("loginbutton")
   },
  message: function(m){
    if(typeof m === "string") mostrador.doms.warnings.innerText = m
  },
  errormessage: function(m,disable){
    mostrador.message(m)
    if(disable) mostrador.doms.button.disabled = true
    mostrador.movimentar()
  },
  limpar: function(){
    mostrador.doms.warnings.innerText = ""
    mostrador.doms.button.disabled = false
  },
  movimentar: function(){
    mostrador.doms.loginbox.style.animation = "error 0.25s ease"
    setTimeout(function(){mostrador.doms.loginbox.style.animation = ""}, 250)
  }
}

/* Verifica o formato da matrícula */
document.getElementById("matricula_input").addEventListener("change", function(e){
  var d = e.target.value
  if(d.split("")[2] != "/") mostrador.errormessage("Formato inválido", true)
  else if(d.length != 10) mostrador.errormessage("Tamanho inválido", true)
  else if(d.match(/[0-9\/]/g).length != d.split("").length) mostrador.errormessage("Caracteres inválidos", true)
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

/* Faz submit do form */
document.getElementById("loginbutton").addEventListener("click", function(e){
  e.preventDefault();
  q.post("home", q.toFormData(e.target.form), new q.actions({
    done: function(){
      window.location = "home"
    },
    fail: function(){
      mostrador.errormessage("Informação errada", false);
    },
    wait: function(){
      mostrador.message("Verificando");
    }
  }))
});
