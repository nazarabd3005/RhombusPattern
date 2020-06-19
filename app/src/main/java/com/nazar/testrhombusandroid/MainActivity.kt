package com.nazar.testrhombusandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import com.nazar.testrhombusandroid.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    //default debounce time value
    private val debounceValue: Long = 200

    //default debounce time unit
    private val debounceTimeUnit = TimeUnit.MILLISECONDS

    private lateinit var binding: ActivityMainBinding

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val disposable = RxTextView.textChanges(binding.edittextNumber)
            .skipInitialValue()
            .debounce(debounceValue, debounceTimeUnit)
            .map { if (it.isNotEmpty()) it.toString().toInt() else 0 }
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->

                binding.textviewRhombus.text = rhombusPatternExecute(result)
            }
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
    private fun rhombusPatternExecute(number: Int): String {
        binding.textinputNumber.error = ""
        return when {
            number < 5 -> {
                binding.textinputNumber.error = "minimal 5"
                ""
            }
            number > 100 -> {
                binding.textinputNumber.error = "maximal 100"
                ""
            }
            else -> {
                val stringBuilder = StringBuilder()
                var space = 0
                for (i in 0 until number / 2) {
                    if (i == 0 || i == number - 1) {
                        if (number % 2 == 1) {
                            stringBuilder.append("*")
                        } else {
                            stringBuilder.append("**")
                        }
                    } else {
                        stringBuilder.append("*")
                    }
                    if (space > 0) {
                        stringBuilder.append(" ".repeat(space))
                        stringBuilder.append("*")
                    }
                    stringBuilder.append(System.getProperty("line.separator"))
                    space++

                }

                if (number % 2 == 0) {
                    space--
                }

                for (i in number / 2 until number) {
                    if (i == number - 1) {
                        if (number % 2 == 1) {
                            stringBuilder.append("*")
                        } else {
                            stringBuilder.append("**")
                        }
                    } else {
                        stringBuilder.append("*")
                    }

                    if (space > 0) {
                        stringBuilder.append(" ".repeat(space))
                        stringBuilder.append("*")
                    }
                    stringBuilder.append(System.getProperty("line.separator"))
                    space--
                }
                stringBuilder.toString()
            }
        }
    }
}