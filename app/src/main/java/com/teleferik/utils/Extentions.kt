package com.teleferik.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.WebViewActivity
import com.teleferik.data.network.Resource
import com.teleferik.models.ErrorResponse
import com.teleferik.models.signupError.SignupErrorResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun ImageView.loadImage(
    url: String?
) {
    if (!url.isNullOrEmpty()) {
        Glide.with(context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade(120))
            .into(this)
    } else this.setImageResource(R.drawable.splash_log)
}


fun ImageView.loadImage(
    file: File
) {

    Glide.with(context)
        .load(file)
        .placeholder(R.drawable.splash_log)
        .into(this)
}

fun <A : Activity> Activity.openNewActivity(activity: Class<A>, isFishable: Boolean = false) {
    Intent(this, activity).also {

        startActivity(it)
        if (isFishable) {
            finish()
        }

    }
}

fun Fragment.handleApiErrors(
    failure: Resource.Failure,
    edtToShowValidation: EditText? = null,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> showTopToast(getString(R.string.check_your_internet_connection_txt))
        failure.errorCode == HTTPCODES.CODEAUTHERROR.code -> {
            showTopToast(getString(R.string.unauthorized))
        }
        failure.errorCode == HTTPCODES.CODE_USER_EXIST_BEFORE.code -> {
            if (failure.errorBody is ResponseBody) {
                val error = failure.errorBody.string()
                val baseResponse = Gson().fromJson(error, SignupErrorResponse::class.java)
                if (edtToShowValidation == null)
                    showTopToast(getString(R.string.failed_operation))
            }
        }
        failure.errorCode == HTTPCODES.CODEINTERNALSERVERERROR.code -> showTopToast(getString(R.string.internal_server_error))
        else -> {
            if (failure.errorBody is ResponseBody) {
                val error = failure.errorBody.string()
                val baseResponse = Gson().fromJson(error, ErrorResponse::class.java)
                showTopToast(baseResponse.errors)
            } else if (failure.errorBody is String) {
                showTopToast(failure.errorBody)
            }
        }
    }
}

fun Fragment.handleApiErrorsLogin(
    failure: Resource.Failure,
    edtToShowValidation: EditText? = null,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> showTopToast(getString(R.string.check_your_internet_connection_txt))
        failure.errorCode == HTTPCODES.CODEAUTHERROR.code -> {
            showTopToast(getString(R.string.unauthorized))
        }
        failure.errorCode == HTTPCODES.CODE_USER_EXIST_BEFORE.code -> {
            if (failure.errorBody is ResponseBody) {
                val error = failure.errorBody.string()
                val baseResponse = Gson().fromJson(error, SignupErrorResponse::class.java)
                if (edtToShowValidation == null)
                    if(baseResponse.errors?.email?.first() == null)
                        showTopToast(getString(R.string.check_your_credentials_or_support))
                    else
                        showTopToast(getString(R.string.welcome,baseResponse.errors?.email?.first()))
                else
                    edtToShowValidation.error = baseResponse.errors?.email?.first()

            }
        }
        failure.errorCode == HTTPCODES.CODEINTERNALSERVERERROR.code -> showTopToast(getString(R.string.internal_server_error))
        else -> {
            if (failure.errorBody is ResponseBody) {
                val error = failure.errorBody.string()
                val baseResponse = Gson().fromJson(error, ErrorResponse::class.java)
                showTopToast(baseResponse.errors)
            } else if (failure.errorBody is String) {
                showTopToast(failure.errorBody)
            }
        }
    }
}


fun <A : Activity> Activity.openNewActivityThenClearStack(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun TextView.countDownTimer(timeInMill: Long, interval: Long, tvResend: TextView, tvTimer: TextView) {
    val context = this.context
    val timer = object : CountDownTimer(timeInMill, interval) {
        override fun onTick(millisUntilFinished: Long) {

            var diff = millisUntilFinished
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            val elapsedDays = diff / daysInMilli
            diff %= daysInMilli

            val elapsedHours = diff / hoursInMilli
            diff %= hoursInMilli

            val elapsedMinutes = diff / minutesInMilli
            diff %= minutesInMilli

            val elapsedSeconds = diff / secondsInMilli

            text = "$elapsedMinutes : $elapsedSeconds "
            tvResend.paintFlags  = 0
            tvResend.isEnabled = false
            tvResend.setTextColor(ResourcesCompat.getColor(context.resources,R.color.gray8,null))

        }

        override fun onFinish() {
            tvResend.setTextColor(ResourcesCompat.getColor(context.resources,R.color.base_app_color,null))
            tvResend.paintFlags = tvResend.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            tvResend.isEnabled = true
            tvTimer.showHideView(false)
        }
    }
    timer.start()
}

fun TextView.setTextEnabled(isEnabled: Boolean) {
    if (isEnabled)
        setTextColor(ContextCompat.getColor(context, R.color.black))
    else
        setTextColor(ContextCompat.getColor(context, R.color.grey))

}

fun View.setViewEnabled(isEnabled: Boolean) {
    if (isEnabled) {
        isVisible = isEnabled
        setBackgroundColor(ContextCompat.getColor(context, R.color.black))
    } else isVisible = isEnabled
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

// get text from edit text ..
fun EditText.captureText() = text.toString()

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun Fragment.getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
    var drawable = ContextCompat.getDrawable(requireActivity(), drawableId)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        drawable = DrawableCompat.wrap(drawable!!).mutate()
    }
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    drawable.draw(canvas)
    return bitmap
}

fun View.snackBar(message: String, action: (() -> Unit)? = null) {
    val snackBar = Snackbar.make(this, message, BaseTransientBottomBar.LENGTH_LONG)
    action?.let {
        snackBar.setAction("Retry") {
            it()
        }
    }
    snackBar.show()
}

val auePhoneRegx = "(1)[0-9]{9}"
fun String.isValidPhone(): Boolean =
    this.isNotEmpty() && this.matches(Regex(auePhoneRegx))


fun View.showHideView(isVisable: Boolean) {
    visibility = if (isVisable)
        View.VISIBLE
    else
        View.GONE
}


fun View.invisibleView(isVisable: Boolean) {
    visibility = if (isVisable)
        View.VISIBLE
    else
        View.INVISIBLE
}

fun getFirstName(name: String): String {

    var firstName = "";
    firstName = if (name.split(" ").size > 1) {
        name.substring(0, name.lastIndexOf(' '));
    } else {
        name
    }
    return firstName


}

fun RecyclerView.getCurrentPosition(): Int {
    return (this.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
}

enum class HTTPCODES(val code: Int) {
    CODEVALIDATION(101),
    CODEACCOUNTNOTVERIFIED(105),
    CODESUCCESS(200),
    CODEURLNOTFOUND(404),
    CODEINTERNALSERVERERROR(500),
    CODEAUTHERROR(401),
    CODE_USER_EXIST_BEFORE(422)
}

fun Fragment.showTopToast(message: String) {
    val t = Toast.makeText(this.context, message, Toast.LENGTH_LONG)
    t.show()
}

fun getDeviceId(): String {
    return Settings.Secure.getString(
        AppController.getInstance()!!.applicationContext.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}

fun ClosedRange<Char>.randomString(length: Int) =
    (1..length)
        .map { (Random().nextInt(endInclusive.toInt() - start.toInt()) + start.toInt()).toChar() }
        .joinToString("")

// add entry in shared preference
fun SharedPreferences.putAny(name: String, any: Any) {
    when (any) {
        is String -> edit().putString(name, any).apply()
        is Int -> edit().putInt(name, any).apply()
        is Boolean -> edit().putBoolean(name, any).apply()
        // also accepts Float, Long & StringSet
    }
}

fun String.buildDeepLink(): String {
    return "android-app://com.teleferik/$this"
}


// remove entry from shared preference
fun SharedPreferences.remove(name: String) {
    edit().remove(name).apply()
}

// remove all keys
fun SharedPreferences.clearAll() {
    edit().clear().apply()
}


fun EditText.setReadOnly(value: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}

fun String.validatePrice(): String {
    return String.format("%.2f", this.toFloat())
}

fun String.isValidEmail(): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun Fragment.checkPermissions(pickerMethod: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (readExternalPermission() == PackageManager.PERMISSION_GRANTED && writeExternalPermission() == PackageManager.PERMISSION_GRANTED && cameraPermission() == PackageManager.PERMISSION_GRANTED) {
            pickerMethod.invoke()
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 123
            )
        }
    } else {
        pickerMethod.invoke()
    }
}

fun Fragment.readExternalPermission(): Int =
    ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)


fun Fragment.writeExternalPermission(): Int =
    ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)


fun Fragment.cameraPermission(): Int =
    ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)

fun Context.getDate(createdDate: String, currentDate: String): String? {
    val formatDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", this.resources.configuration.locale)
    formatDate.timeZone = TimeZone.getTimeZone("UTC")
    val formatHours = SimpleDateFormat("HH:mm", Locale("en"))
    formatHours.timeZone = TimeZone.getDefault()
    try {
        val mCreatedDate = formatDate.parse(createdDate)
        val mCurrentDate = formatDate.parse(currentDate)
        val mCreatedHours = formatHours.format(mCreatedDate)
        //milliseconds
        var different = mCurrentDate.time - mCreatedDate.time
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different %= daysInMilli

        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli

        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli

        return when{
            elapsedDays == 1L -> this.getString(R.string.yesterday_date,"$mCreatedHours")
            elapsedDays == 0L -> this.getString(R.string.since_hrs,elapsedHours.toString())
            elapsedDays > 1L -> this.getString(R.string.since_days,elapsedDays.toString())
            elapsedDays == 0L && elapsedHours == 0L -> this.getString(R.string.since_mins,elapsedMinutes.toString())
            else -> "$mCreatedDate"
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return null
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val currentTime = Calendar.getInstance().time
    return sdf.format(currentTime)
}

fun Context.openWebView(url:String)
{
    val intent = Intent(this, WebViewActivity::class.java)
    intent.putExtra(
        Constants.PARAMS.SCREEN_TITLE,
        ""
    )
    intent.putExtra(Constants.PARAMS.SCREEN_URL, url)
    startActivity(intent)
}

/**
 * Adding Dots to Slider
 * @param currentPage current selected View over the ViewPager
 */
fun addViewPagerBottomDots(
    context: Context,
    currentPage: Int,
    length: Int,
    margin: Int,
    layoutDots: LinearLayoutCompat
) {
    val dots: Array<AppCompatImageView?> = arrayOfNulls<AppCompatImageView>(length)
    layoutDots.removeAllViews()
    val marginValue: Int = digitalPointToPixel(context, margin)
    for (i in dots.indices) {
        dots[i] = AppCompatImageView(context)
        val params: LinearLayoutCompat.LayoutParams = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
        )
        params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        params.marginEnd = marginValue
        params.marginStart = marginValue
        dots[i]?.layoutParams = params
        dots[i]?.setImageDrawable(ResourcesCompat.getDrawable(context.resources,R.drawable.ic_dot_inactive,context.theme))
        layoutDots.addView(dots[i])

    }
    if (dots.isNotEmpty()) {
        dots[currentPage]?.setImageDrawable(ResourcesCompat.getDrawable(context.resources,R.drawable.ic_dot_active,context.theme))
    }
}

fun digitalPointToPixel(context: Context, dp: Int): Int {
    // Converts 14 dip into its equivalent px
    val r = context.resources
    val px = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        r.displayMetrics
    )
    return px.toInt()
}

fun Activity.hideKeyboard(): Boolean {
    return (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
}

fun Fragment.hideKeyboard(): Boolean {
    return (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow((activity?.currentFocus ?: View(context)).windowToken, 0)
}

fun EditText.hideKeyboard(): Boolean {
    return (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.showKeyboard(): Boolean {
    return (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .showSoftInput(this, 0)
}