package ru.neosvet.dictionary

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler

object Schedulers {
    fun background(): Scheduler = io.reactivex.rxjava3.schedulers.Schedulers.io()

    fun main(): Scheduler = AndroidSchedulers.mainThread()
}