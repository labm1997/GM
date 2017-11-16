 var mobile = false;
 
 var q = {
  nothing: function(){},
  actions: function(object){
    this.wait = this.done = this.fail = q.nothing;
    if(typeof(object.wait) === "function") this.wait = object.wait;
    if(typeof(object.done) === "function") this.done = object.done;
    if(typeof(object.fail) === "function") this.fail = object.fail;
  },
  toFormData: function(data){
    var form = new FormData();
    if(data.constructor === HTMLFormElement) 
      for(var i=0;i<data.length;i++) form.set(data[i].name,data[i].value);
    else if(typeof(data) === "object") for(a in data) form.set(a,data[a]);
    else return "Falha ao executar a função"
    return form;
  },
  post: function(url, form, f){
	  var a=new XMLHttpRequest(); 
	  
	  if(typeof(form) !== "object" || typeof(url) !== "string") return "Falha ao enviar";
	  a.open("POST",url,true);
	  a.onreadystatechange = function(){
			if(f && f.constructor === q.actions) {
		    if(a.readyState == a.DONE) {
				    if(a.status === 200 || a.status == 201) f.done(a.responseText)
				    if(a.status >= 400 && a.status <= 410) f.fail(a.responseText)
				    if(parseInt(a.status/100)%5 == 0) alert("Erro de servidor");
				}
				else f.wait();
			}
	  }
	  a.send(form)
  },
  get: function(url, f){
	  var a=new XMLHttpRequest(); 
	  
	  a.open("GET",url,true);
	  a.onreadystatechange = function(){
			if(f && f.constructor === q.actions) {
		    if(a.readyState == a.DONE) {
				    if(a.status === 200) f.done(a.responseText)
				    if(a.status >= 400 && a.status <= 410) f.fail(a.responseText)
				    if(parseInt(a.status/100)%5 == 0) alert("Erro de servidor");
				}
				else f.wait();
			}
	  }
	  a.send()
  }
 }

/* Oculta/Mostra leftbar */
function hideShowLeftBar(e){
	var fixedbar = document.getElementsByClassName("leftbar")[0];
  if(fixedbar.hidden) {
    if(e) {
      fixedbar.style.animation = "show .5s forwards";
      fixedbar.hidden = false;
    }
  }
  else {
    fixedbar.style.animation = "hide .5s forwards";
    fixedbar.hidden = true;
  }
}

/* Detecta o tamanho da tela */
function screenSelect() {
	var w = screen.width * (window.devicePixelRatio || 1);
	//var il = document.getElementById("inner_login");
	//var cronograma = document.getElementById("cronograma");
	var fixedbar = document.getElementsByClassName("leftbar")[0];
	var topbar = document.getElementsByClassName("topbar")[0];
	var logo = document.getElementsByClassName("logo")[0];
	var pagestructure = document.getElementsByClassName("pagestructure")[0];
	var loginform = document.getElementById("loginform");
	var startx = 0;
	
	if (w > 1200) mobile = false;
	else if(mobile == false) {
	  //cronograma.style.display = "none"
	  //il.style.width = "80%"
	  fixedbar.style.position = "fixed";
	  fixedbar.style.paddingTop = "57px";
	  topbar.style.position = "fixed";
	  logo.className += " logoleft";
	  logo.addEventListener("click", function(){hideShowLeftBar(true)});
	  pagestructure.addEventListener("click", function(){hideShowLeftBar(false)});
	  pagestructure.addEventListener("touchstart", function(e){startx = e.changedTouches[0].clientX});
	  pagestructure.addEventListener("touchend", function(e){if(startx && e.changedTouches[0].clientX-startx > 30 && startx < 100) hideShowLeftBar(true)});
	  if(loginform) loginform.style.width = "100%";
	  mobile = true;
	}
}



setInterval(screenSelect, 100);
