package edu.luc.etl.ccacw.sensor

import scalaz.{ Show, Tree, TreeLoc }
import scalaz.std.option._
import scalaz.syntax.tree._

package object model {

  implicit object shr extends Show[Resource] { override def shows(r: Resource) = r.toString }

  implicit object shl extends Show[TreeLoc[Resource]] { override def shows(r: TreeLoc[Resource]) = "->" + r.toString }

  implicit val r2t = ToTreeOps[Resource] _

  // TODO validation of domain model
  // TODO auto-generate routes

  implicit def dummy = new Resource { val name = "" }

  def descend[T <: { def name: String }](path: Iterable[String])(locs: Stream[Tree[TreeLoc[T]]])(implicit dummy: T): Option[Tree[TreeLoc[T]]] =
    path.foldLeft {
      some(dummy.leaf.loc.node(locs.toList: _*))
    } { (l, s) =>
      l flatMap {
        _.subForest find { _.rootLabel.getLabel.name == s }
      }
    }
}