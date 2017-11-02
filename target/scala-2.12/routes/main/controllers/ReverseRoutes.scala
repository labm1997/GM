
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/luiz/Documentos/TP1/Projeto Final/GM/conf/routes
// @DATE:Thu Nov 02 15:05:05 BRST 2017

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:7
package controllers {

  // @LINE:7
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:12
    def dadosPessoais(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "dados/dados_pessoais")
    }
  
    // @LINE:9
    def sessionStart(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "home")
    }
  
    // @LINE:11
    def sessionEnd(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "logout")
    }
  
    // @LINE:14
    def historicoEscolar(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "dados/historico_escolar")
    }
  
    // @LINE:10
    def home(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "home")
    }
  
    // @LINE:15
    def cursos(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "dados/cursos")
    }
  
    // @LINE:13
    def gradeHoraria(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "dados/grade_horaria")
    }
  
    // @LINE:7
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
    // @LINE:8
    def login(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "login")
    }
  
  }

  // @LINE:18
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:18
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }


}
