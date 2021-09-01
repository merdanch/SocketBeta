package tm.com.beletfilm.socketbeta.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tm.com.beletfilm.socketbeta.Message
import tm.com.beletfilm.socketbeta.SocketHandler
import tm.com.beletfilm.socketbeta.model.ConnectionStatus
import tm.com.beletfilm.socketbeta.nameValuePairs

class MainViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _isNewMessage = MutableLiveData<Boolean>()
    val isNewMessage: LiveData<Boolean>
        get() = _isNewMessage

    private val _sendSms = MutableLiveData<Message>()
    val sendSms: LiveData<Message>
        get() = _sendSms

    private val _connectionStatus = MutableLiveData<ConnectionStatus>()
    val connectionStatus: LiveData<ConnectionStatus>
        get() = _connectionStatus

    fun prepSocket() {

        _connectionStatus.postValue(ConnectionStatus.CONNECTING)

        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        val mSocket = SocketHandler.getSocket()

        val gson = Gson()

        mSocket.on("message") { args ->
            if (args[0] != null) {

                val a = gson.toJson(args[0])
                val res = gson.fromJson(a, nameValuePairs::class.java)

                val phone = "+993" + res.nameValuePairs.phone
                val message = res.nameValuePairs.message

                _sendSms.postValue(Message(phone, message))
                _isNewMessage.postValue(true)

                Log.e("SOCKET EVENT MESSAGE", phone)
                Log.e("SOCKET EVENT MESSAGE", message)

                mSocket.emit("success")

                _isNewMessage.postValue(false)
            }
        }

        mSocket.on(Socket.EVENT_CONNECT) {
            _connectionStatus.postValue(ConnectionStatus.CONNECTED)
        }.on(Socket.EVENT_DISCONNECT) {
            _connectionStatus.postValue(ConnectionStatus.DISCONNECTED)
        }.on(Socket.EVENT_CONNECT_ERROR) {
            _connectionStatus.postValue(ConnectionStatus.CONNECTION_ERROR)
        }


    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}