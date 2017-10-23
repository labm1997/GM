
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/luiz/Documentos/TP1/Projeto Final/GM/conf/routes
// @DATE:Sun Oct 22 22:59:02 BRST 2017


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
