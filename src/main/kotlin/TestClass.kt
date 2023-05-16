import com.example.data.DatabaseProvider
import com.example.data.dao.CouponDAO
import java.util.*

fun main() {
    val dao = CouponDAO
    dao.dropTable()
}