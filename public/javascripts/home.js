function geClass(str){
  return [].slice.call(document.getElementsByClassName(str))
}

var pageload = document.getElementById("loadablepage");
var options = [];
var back = [];

/* Carrega uma pagina */
function loadPage(destine,f){
  q.get(destine.href, new q.actions({
    done: function(d){
      document.body.style.cursor=""
      if(typeof(f) === "function") f();
      if(pageload) pageload.innerHTML = "<h1 class='leftalign'>" + destine.name + "</h1>" + d;
      back.push({name: destine.name, href: destine.href})
    },
    wait: function(){document.body.style.cursor="progress"},
    fail: function(){document.body.style.cursor=""}
  }))
}


/* Cria um evento para as opções */
function optionsListener(array){
  array.forEach(function(a){
    a.addEventListener("click", function(e){
      e.preventDefault()
      var info = {name: a.getAttribute("name") || a.innerText, href: e.target.getAttribute("href")}, destine
      
      /* Verifica se href é de voltar */
      if(info.href == "voltar") {
        back.pop();
        destine = back.pop();
      }
      else destine = info;
      
      /* Para POST */
      console.log(e.target.form)
      if(e.target.type == "submit" && e.target.form) 
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
        if(destine.href) loadPage(destine, function(){
          array.forEach(function(b) {b.style.color=""})
          e.target.style.color="rgba(255,255,255,1)"
        })
       
    })
    
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
