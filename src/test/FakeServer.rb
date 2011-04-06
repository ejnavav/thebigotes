require 'socket'              

server = TCPServer.open(54321)   # Socket to listen on port 2000
clients = [];

puts "Server started"
# Process.daemon()

1000.times do
  clients << server.accept
  puts "A client connected"
  
  Thread.start(clients) do |clients|
    client = clients.last
    player = "p#{clients.size}"
    loop do
      puts "Enter command:"
      command = gets
      client.puts command
      reply = client.gets
      puts player + " replied: " + reply
    end
  end
end