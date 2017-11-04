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
				    if(a.status === 200) f.done(a.responseText)
				    if(a.status >= 400 && a.status <= 410) f.fail(a.responseText)
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
				}
				else f.wait();
			}
	  }
	  a.send()
  }
 }
