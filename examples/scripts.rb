
# generate all position options
positions = []
['h', 'v'].each do |orientation|
  ('a'..'f').each do |letter|
    (1..6).each do |number| 
      positions << "#{orientation}@#{letter}#{number}"
    end
  end
end
puts positions.join(",").split