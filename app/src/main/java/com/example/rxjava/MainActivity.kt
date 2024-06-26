package com.example.rxjava

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rxjava.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.merge
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mDisposable: Disposable
    lateinit var compositeDisposable: CompositeDisposable
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        foo()
//        fooRx()
//        fromRX()
//        intervals()
//        timer()
//        distinct()
//        buffer()
//        map()
//        concat()
        merge()
    }


    // Normal assign variables
    private fun foo() {
        var foo = 5
        Log.i("TAG_test", "This is an info $foo")
        foo = 40
        Log.i("TAG_test", "This is an info $foo")
        foo = 70
        Log.i("TAG_test", "This is an info $foo")
        foo = 80
        Log.i("TAG_test", "This is an info $foo")
        foo = 90
        Log.i("TAG_test", "This is an info $foo")
        foo = 100
        Log.d("TAG_test", "This is a debug $foo")
        Log.i("TAG_test", "This is an info $foo")
        Log.w("TAG_test", "This is a warning $foo")
        Log.e("TAG_test", "This is an error $foo")
    }


    // Use RxJava

    @SuppressLint("CheckResult")
    private fun fooRx() {
        val fooRX = Observable.just(5, 40, 70, 80, 90, 100)

//        val observer = object : Observer<Int>{
//            override fun onSubscribe(d: Disposable) {
//                Log.d("TAG_RX", "onSubscribe: ")
//            }
//
//            override fun onError(e: Throwable) {
//                Log.e("TAG_ERROR", "onError: $e", )
//            }
//
//            override fun onComplete() {
//                Log.d("TAG", "onComplete: ")
//            }
//
//            override fun onNext(t: Int) {
//                Log.i("TAG", "onNext: $t")
//            }
//        }
//        fooRX.subscribe(observer)


        //Enhance Code Use Lambda with Observable

        fooRX.subscribe { t -> Log.i("TAG", "onNext: $t");Log.d("TAG", "onComplete: ") }
    }


    //RX Java from operator
    @SuppressLint("CheckResult")
    private fun fromRX() {
        val fromRX = Observable.fromArray(2, 4, 6, 84, 56, 324, 634, 8, 0, 11)

        fromRX.subscribe { t -> Log.i("TAG", "onNext: $t");Log.d("TAG", "onComplete: ") }
        val list = listOf("A", "B", "C")
        // To use repeat operator
        val fromList = Observable.fromIterable(list).repeat(2)
        // To use range operator
        val fromList2 = Observable.range(1, 50)

        fromList.subscribe { t -> Log.i("TAG_LIST", "onNext: $t") }
        fromList2.subscribe { t -> Log.i("TAG_LIST", "onNext: $t") }


    }


    //RX Java intervals       & take
    @SuppressLint("CheckResult")
    private fun intervals() {
        // Put take() to stop when the first interval count = 1000    -> 999
        val intervals = Observable.interval(60, TimeUnit.MICROSECONDS)
            .take(1000)//or u can use TimeUnit.MICROSECONDS or takeLast

        intervals.subscribe { t -> Log.i("TAG_INTERVAL", "onNext: $t") }
    }


    // Use Timer Operator
    @SuppressLint("CheckResult")
    private fun timer() {
        // TO Show onNext After 5 seconds and repeat it
        val timer = Observable.timer(5, TimeUnit.SECONDS).repeat()
        timer.subscribe { t -> Log.i("TAG_TIMER", "onNext: $t") }
    }


    // Use Distinct Operator to remove repeated values
    @SuppressLint("CheckResult")
    private fun distinct() {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 10)
        val distinct = Observable.fromIterable(list).distinct()
        distinct.subscribe() { t -> Log.i("TAG_DISTINCT", "$t") }
    }


    // Use Buffer Operator to put every 4 elements in list
    @SuppressLint("CheckResult")
    private fun buffer() {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 10)
        val buffer = Observable.fromIterable(list).distinct().buffer(4)
        buffer.subscribe() { t -> Log.i("TAG_DISTINCT", "$t") }
    }


    // Use MAp Operator to change the value like we want
    @SuppressLint("CheckResult")
    private fun map() {
        val map = Observable.range(1, 20).map { it * 2 }
        map.subscribe() { t -> Log.i("TAG_DISTINCT", "$t") }
    }


    // Use Concat Operator to return other Observables without interleaved (Link between Observables)
    @SuppressLint("CheckResult")
    private fun concat() {
        val sumObservable = Observable.interval(3, TimeUnit.MILLISECONDS).take(10).map { it + 2 }
        val concat = Observable.interval(3, TimeUnit.SECONDS).take(10).map { it * 2 }
            .concatWith(sumObservable)

        concat.subscribe() { t -> Log.i("TAG_DISTINCT", "$t") }
    }

    // Use Merge operator to merge between Observables without waiting
    @SuppressLint("CheckResult")
    private fun merge() {
        val sumObservable = Observable.interval(3, TimeUnit.MILLISECONDS).take(10).map { it + 2 }
        val merge = Observable.interval(3, TimeUnit.SECONDS).take(10).map { it * 2 }
            .mergeWith(sumObservable)
        compositeDisposable.add(merge.subscribe() { t -> Log.i("TAG_DISTINCT", "$t") })

    }


    // Switch between different threads
    @SuppressLint("CheckResult")
    private fun schedulers() {
        val schedulers = Observable.range(1, 200)
        // IO for simple long operations
        // Computation for Complicated operations (need CPU)

        //subscribeOn Stay on same thread
        //observeOn go to another thread

        schedulers.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        schedulers.subscribe() { t ->
            Log.i("TAG", "$t - ${Thread.currentThread().name}")
        }
    }


    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }


}
