package org.example.utils

class UrlUtils {
    companion object {
        fun getCouponAPI(courseId: Int, couponCode: String): String {
            return "https://www.udemy.com/api-2.0/course-landing-components/${courseId}/me/?couponCode=${couponCode}&components=deal_badge,discount_expiration,gift_this_course,price_text,purchase,recommendation,redeem_coupon,cacheable_deal_badge,cacheable_discount_expiration,cacheable_price_text,cacheable_buy_button,buy_button,buy_for_team,cacheable_purchase_text,cacheable_add_to_cart,money_back_guarantee,instructor_links,incentives_context,top_companies_notice_context,curated_for_ufb_notice_context,sidebar_container,purchase_tabs_context,subscribe_team_modal_context,lifetime_access_context,available_coupons"
        }

        fun getCourseAPI(courseId: Int): String {
            return "https://www.udemy.com/api-2.0/courses/${courseId}/?fields[course]=title,context_info,primary_category,primary_subcategory,avg_rating_recent,visible_instructors,locale,estimated_content_length,num_subscribers,num_reviews,description,headline,instructional_level"

        }
    }
}