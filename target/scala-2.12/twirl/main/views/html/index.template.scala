
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.4*/("""

"""),_display_(/*3.2*/main("Bem vindo(a) ao GM")/*3.28*/ {_display_(Seq[Any](format.raw/*3.30*/("""
  """),format.raw/*4.3*/("""<h1>GM - Gerenciador de Matrículas</h1>
  Roteiro de evolução do projeto: 
  <ul>
    <li><a href="assets/doc/Roteiro_20171022.pdf">Roteiro_20171022.pdf</a></li>
    <li><a href="assets/doc/Roteiro_20171022.tex">Roteiro_20171022.tex</a></li>
  </ul>
""")))}),format.raw/*10.2*/("""
"""))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Wed Oct 25 12:15:03 BRST 2017
                  SOURCE: /home/luiz/Documentos/TP1/Projeto Final/GM/app/views/index.scala.html
                  HASH: fae6daef0760793b18538def58ff69c1333fbcfc
                  MATRIX: 722->1|818->3|846->6|880->32|919->34|948->37|1229->288
                  LINES: 21->1|26->1|28->3|28->3|28->3|29->4|35->10
                  -- GENERATED --
              */
          