function geClass(str){
  return [].slice.call(document.getElementsByClassName(str))
}

var pageload = document.getElementById("loadablepage");
var options = [];
var back = [{name: "", href: "", scroll: null}];

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
    fail: function(){document.body.style.cursor=""}
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
      done: function(){
        alert("Foi!")
        loadPage(back.pop())
      },
      fail: function(){
        alert("Não Foi!")
      },
      wait: function(){
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
