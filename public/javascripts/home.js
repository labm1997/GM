function geClass(str){
  return [].slice.call(document.getElementsByClassName(str))
}

var pageload = document.getElementById("loadablepage");
var options = [];
var back = [{name: "", href: "", scroll: null}];

/* Mensagem de tela inteira */
var fullBox = {
  DOM: document.getElementsByClassName("fullbox")[0],
  windowList: [document.getElementsByClassName("window")[0], document.getElementsByClassName("windowtop")[0], document.getElementsByClassName("windowtext")[0], document.getElementsByClassName("windowlabel")[0]],
  close: function(){
    this.DOM.style.display = "none"
    this.windowList[1].className = "windowtop"
  },
  show: function(label, message, className){
    this.windowList[3].innerText = label
    this.windowList[2].innerHTML = message
    this.windowList[1].className += " " + className || ""
    this.DOM.style.display = "block"
  }
}

fullBox.DOM.addEventListener("click", function(e){
 if(fullBox.windowList.indexOf(e.target) == -1) fullBox.close()
})

/* Mensagem de sinalizador */
var sinalizador = {
  DOM: document.getElementById("sinalizador"),
  show: function(message){
    this.DOM.innerText = message
  }
}

/* Carrega uma pagina */
function loadPage(destine,f){
  q.get(destine.href, new q.actions({
    done: function(d){
      document.body.style.cursor=""
      if(typeof(f) === "function") f();
      if(pageload) {
        if(back.length && !back[back.length-1].scroll) back[back.length-1].scroll = document.body.scrollTop;
        back.push({name: destine.name, href: destine.href, scroll: null})
        pageload.innerHTML = "<h1 class='leftalign'>" + destine.name + "</h1>" + d;
        if(destine.scroll) document.body.scrollTop = destine.scroll
        else document.body.scrollTop = 0
      }
    },
    wait: function(){document.body.style.cursor="progress"},
    fail: function(){document.body.style.cursor=""},
    serverError: function(m){
      document.body.style.cursor=""
      fullBox.show("Falha grave", "Erro no servidor, veja a falha completa <a target='_blank' href=\"data:text/html;charset=utf-8,"+encodeURIComponent(m)+"\">aqui</a>", "windowtopServerError")
    }
  }))
}

/* Função a ser executada quando o evento ocorrer */
function eventAction(e,a,array,post){
  e.preventDefault()
  var info = {name: a.getAttribute("name") || a.innerText, href: e.target.getAttribute("href"), scroll: null}, destine,scroll
  
  /* Verifica se href é de voltar */
  if(info.href == "voltar") {
    console.log(back.pop());
    destine = back.pop();
    console.log(destine)
  }
  else destine = info;
  
  /* Para POST */
  //console.log(e.target.form)
  if(post && e.target.form) 
    q.post(e.target.form.action, q.toFormData(e.target.form), new q.actions({
      done: function(m){
        fullBox.show("Sucesso", m)
        sinalizador.show("")
        loadPage(back.pop())
      },
      fail: function(m){
        fullBox.show("Falha", m, "windowtopError")
        sinalizador.show("")
      },
      serverError: function(m){
        fullBox.show("Falha grave", "Erro no servidor, veja a falha completa <a target='_bottom' href=\"data:octet-stream;base64,"+btoa(m)+"\">aqui</a>", "windowtopServerError")
        sinalizador.show("")
      },
      wait: function(){
        sinalizador.show("Atualizando...")
      }
    }))
  
  /* Para GET */
  else
    if(destine && destine.href) loadPage(destine, function(){
      array.forEach(function(b) {b.style.color=""})
      e.target.style.color="rgba(255,255,255,1)"
    })
    else pageload.innerHTML = ""
   
}

/* Cria um evento para as opções */
function optionsListener(array){
  array.forEach(function(a){
    if(a.type == "number") a.addEventListener("change", function(e){eventAction(e,a,array,true)})
    else if(a.type == "submit") a.addEventListener("click", function(e){eventAction(e,a,array,true)})
    else a.addEventListener("click", function(e){eventAction(e,a,array)})
    
    /* Deixa a opção branca */
    a.addEventListener("click", function(e){
      var open = e.target.getAttribute("open")
      var close = e.target.getAttribute("close")
      if(open) geClass(open).forEach(function(b){
        b.style.display = "block"
        e.target.setAttribute("open",null)
        e.target.setAttribute("close",open)
      })
      if(close) geClass(close).forEach(function(b){
        b.style.display = "none"
        e.target.setAttribute("close",null)
        e.target.setAttribute("open",close)
      })
    })
    
  })
}

/* Fica a procura de novos "listener" */
setInterval(function(){
  var noptions = geClass("listener")
  var diff = noptions.filter(function(op){return options.indexOf(op) < 0})
  optionsListener(diff)
  options = noptions
},100)
