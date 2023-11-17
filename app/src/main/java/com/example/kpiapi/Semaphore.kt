import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock

class Semaphore(private val permits: Int) {
    private val mutex = Mutex()

    private var availablePermits = permits

    suspend fun acquire() {
        mutex.withLock {
            while (availablePermits == 0) {
                mutex.unlock()
                mutex.lock()
            }
            availablePermits--
        }
    }

    suspend fun release() {
        mutex.withLock {
            availablePermits++
        }
    }
}