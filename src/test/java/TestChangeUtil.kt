import cc.origind.server.update.Change
import cc.origind.server.util.ChangeUtil
import java.util.*

fun main() {
    val changes = ArrayList<Change>()
    changes.add(Change("delete", "mods/XRay.jar"))
    changes.add(Change("upgrade", "mods/AE-old.jar", "mods/AE.jar"))
    System.out.println(ChangeUtil.merge(changes))
}