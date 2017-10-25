
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/vinicius/Documentos/tp1/GM/conf/routes
// @DATE:Wed Oct 25 11:40:55 BRST 2017


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
