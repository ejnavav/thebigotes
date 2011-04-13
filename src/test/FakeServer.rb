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
      input = gets      
      if input =~ /join/ 
        command = join
      else
        command = input
      end
      client.puts command
      reply = client.gets
      puts player + " replied: " + reply
    end
  end
end

def join
  "command:join&options:p,v&message:Join as p) player v) Visitor. \nEnter option"
end