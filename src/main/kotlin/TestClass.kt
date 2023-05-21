import com.example.controller.UdemyCouponCourseExtractor
import com.example.data.dao.CouponDAO
import com.example.data.dao.ExpiredCouponDAO
import com.example.data.dao.LogFetchCouponDao
import com.example.data.dao.LogRequestCouponDao
import com.example.utils.UrlUtils
import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
//    CouponDAO.dropTable()
//    ExpiredCouponDAO.dropTable()
//    LogFetchCouponDao.dropTable()
//    LogRequestCouponDao.dropTable()


//    val couponUrl ="https://www.udemy.com/course/learn-javascript-for-beginners-v/?couponCode=YOUACCEL66804"
// val data =    UdemyCouponCourseExtractor(couponUrl ).getFullCouponCodeData()
//    print(data)

 val url =     UrlUtils.decodeBase64String("aHR0cHM6Ly93d3cudWRlbXkuY29tL2NvdXJzZS9leGNlbC1leGFtLXByZXAtNC1wcmFjdGljZS10ZXN0cy1pbmNsdWRlZC8/Y291cG9uQ29kZT1CMTU2NTFDRjlBMEY2OTVBRjE3Ng==")
    println(url)
}