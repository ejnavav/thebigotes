#Testing from malakas

require 'socket'              

server = TCPServer.open(56789)   # Socket to listen on port 2000
clients = [];

puts "Server started"
# Process.daemon()

loop do
  clients << server.accept
  puts "A client connected"
  Thread.start(clients) do |clients|
    client = clients.last
    nick = client.gets.chop
    puts "#{nick} joined"
    client.puts "Welcome to the chat"
    loop do         
      message = "[#{nick}] " + client.gets
      puts message
      clients.each {|client| client.puts message }
    end
  end
end