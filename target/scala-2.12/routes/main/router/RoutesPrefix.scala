
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/luiz/Documentos/TP1/Projeto Final/GM/conf/routes
// @DATE:Thu Nov 02 17:30:50 BRST 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
