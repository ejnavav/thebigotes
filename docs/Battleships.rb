class BattleShipsServer
  def start
    @game = Battleships.new()
    @listener = ConnectionListener.new(port, @game).start()
  end
end


class ConnectionListener < Thread
  def initialize(port, game)
    @port = port
    @game = game
  end

  def start
    loop do   
      socket = wait_until_connection()
      client = BattleshipsClient.new(socket)  
      # must be async
      @game.handle_client(client) 
    end
  end
end

class BattleShips

  def start
    @player1 = nil
    @player1 = nil
    @who_has_turn = nil
    @player1board = Board.new()
    @player2board = Board.new()
  end

  def handle_client(client)
    @game_controllers << BatleshipsController.new(client)
    @game_controllers.last().start()
  end
end

class BatleshipsController < Thread
  def initialize(client)
    @client = client
  end

  def start
      join(player)
    
  end
  
  def send_join
    player.send("command:join")
  end

  def join(reply)
    if reply = p
      if player1.nil?
        player1 = @client
      elsif player2.nil?
        player2 = @client
      else
        @client.send("Sorry two people playing already")
        return
      end
    end  
  end
end