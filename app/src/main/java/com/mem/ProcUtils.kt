package com.mem

import android.os.Process
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader

object ProcUtils {

    private val pid = Process.myPid()

    /**
     * 示例：0 1 10 11 12 13 14 15 16 17 18
     */
    fun getPidFd(): String {
        var content = ""
        val cmd = "ls /proc/$pid/fd"
        val p = Runtime.getRuntime().exec(cmd)
        BufferedReader(InputStreamReader(p.inputStream)).useLines {
            content = it.joinToString(separator = " ")
        }
        return content
    }


    /**
     * 获取 /proc/[pid]/maps 内容，例如：
     * ```
     * 7234c000-7235f000 r--p 00000000 fc:00 1499          /system/framework/x86/boot-conscrypt.oat
     * 7235f000-72390000 r-xp 00013000 fc:00 1499          /system/framework/x86/boot-conscrypt.oat
     * 72390000-72391000 rw-p 00000000 00:00 0             [anon:.bss]
     * ```
     */
    fun getPidMaps(): String {
        var content = ""
        val cmd = "cat /proc/$pid/maps"
        val p = Runtime.getRuntime().exec(cmd)
        BufferedReader(InputStreamReader(p.inputStream)).useLines {
            content = it.joinToString(separator = "\n")
        }
        return content
    }

    /**
     * 比 /proc/[pid]/maps 可读性更强的 pmap 命令输出
     *
     * 输出示例：
     * ```
     * b3d61000   1048K rw---    [anon:stack_and_tls:10752]
     * b3e67000      8K -----    [anon]
     * b3e6a000   1048K rw---    [anon:stack_and_tls:10751]
     * ```
     */
    fun getPmap(): String {
        var content = ""
        val cmd = "pmap -x $pid"
        val p = Runtime.getRuntime().exec(cmd)
        BufferedReader(InputStreamReader(p.inputStream)).useLines {
            content = it.joinToString(separator = "\n")
        }
        return content
    }

    /**
     *
     */
    fun getSmaps(): String {
        var content = ""
        val cmd = "cat /proc/$pid/smaps"
        val p = Runtime.getRuntime().exec(cmd)
        BufferedReader(InputStreamReader(p.inputStream)).useLines {
            content = it.joinToString(separator = "\n")
        }
        return content
    }

    fun getVmSizeMB(): Long {
        val pid = Process.myPid()
        try {
            BufferedReader(FileReader("/proc/$pid/stat"), 1024).use { pidReader ->
                val line = pidReader.readLine()
                val results = line.split(" ").toTypedArray()
                if (results.size > 22) {
                    return results[22].toLong() / 1024 / 1024
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return -1
    }

}