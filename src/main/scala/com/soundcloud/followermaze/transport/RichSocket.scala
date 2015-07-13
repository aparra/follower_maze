package com.soundcloud.followermaze.transport

import java.io.{ BufferedReader, InputStreamReader, PrintWriter }
import java.net.{ Socket, ServerSocket } 

object RichSocket {
  implicit class Patching(socket: Socket) {

    def userId: Int = readLine.toInt
    
    def readLine: String = createBuffer.readLine()
    
    def createBuffer: BufferedReader = {
      new BufferedReader(new InputStreamReader(socket.getInputStream)) 
    }
    
    def send(content: String) {
      val autoFlush = true
      new PrintWriter(socket.getOutputStream, autoFlush).println(content)
    }
  }
  
  def createServerFor(port: Int): ServerSocket = {
    val serverSocket = new ServerSocket(port)
    serverSocket.setReuseAddress(true)
    serverSocket
  }
}