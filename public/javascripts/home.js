function geClass(str){
  return [].slice.call(document.getElementsByClassName(str))
}

var pageload = document.getElementById("loadablepage");
var options = geClass("option");

options.forEach(function(a){
  a.addEventListener("click", function(e){
    if(e.target.getAttribute("href")) q.get(e.target.getAttribute("href"), new q.actions({
      done: function(d){
        document.body.style.cursor=""
        options.forEach(function(b) b.style.color="")
        e.target.style.color="rgba(255,255,255,1)"
        if(pageload) pageload.innerHTML = "<h1 class='leftalign'>" + a.innerText + "</h1>" + d;
      },
      wait: function(){document.body.style.cursor="progress"},
      fail: function(){document.body.style.cursor=""}
    }))
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
