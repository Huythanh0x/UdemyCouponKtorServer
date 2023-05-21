import com.example.controller.UdemyCouponCourseExtractor
import com.example.data.dao.CouponDAO
import com.example.data.dao.ExpiredCouponDAO
import com.example.data.dao.LogFetchCouponDao
import com.example.data.dao.LogRequestCouponDao
import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    CouponDAO.dropTable()
    ExpiredCouponDAO.dropTable()
    LogFetchCouponDao.dropTable()
    LogRequestCouponDao.dropTable()
//    val couponUrl ="https://www.udemy.com/course/learn-javascript-for-beginners-v/?couponCode=YOUACCEL66804"
// val data =    UdemyCouponCourseExtractor(couponUrl ).getFullCouponCodeData()
//    print(data)
}