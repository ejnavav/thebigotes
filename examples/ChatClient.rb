require 'socket'      # Sockets are in standard library

port = 56789

# puts "Enter host name:"
# host = gets.chop
# host = 'localhost'
host = 'yallara.cs.rmit.edu.au'

socket = TCPSocket.open(host, port)

puts "Enter your nickname:"
socket.puts gets

Thread.start(socket) {
  loop do 
    puts socket.gets
  end
}

loop do
  socket.puts gets
end
# s.close


