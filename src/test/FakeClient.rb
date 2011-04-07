require 'socket'      # Sockets are in standard library

port = 54321
# host = 'localhost'
host = '10.1.1.6'
socket = TCPSocket.open(host, port)

Thread.start(socket) {
  loop do 
    puts socket.gets
  end
}

loop do
  socket.puts gets
end
# s.close