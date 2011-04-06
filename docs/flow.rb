
class listener < Thread
  socket = wait_until_connection()
  client = new Client(socket)
  game_controller = new GameController(client)
  server.add_game_controller(game_cotroller)
  game_controller.start()
end

class game_controller
  
  run()
    command = game.new_player(client)
    
    if command = join
        reply = client.join()
        game.join(reply)
    end
    
  end
  handle_reply(Client, Reply)
end


class client

  join()
    
    do_after(send(join_message))
    return true;
  
  
  send()
    send_meesage
    reply wait_for_response
    game_cotroller.handle_reply(this, reply)
end