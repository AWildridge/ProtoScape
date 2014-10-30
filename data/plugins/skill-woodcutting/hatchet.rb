require 'java'
java_import 'org.apollo.game.model.Animation'

# Missing the dragon hatchet will be added soon.
# Credits ViperSniper & itemdb
# Links: http://runescape.wikia.com/wiki/Hatchet & www.itemdb.biz

HATCHET = {}
HATCHET_ID = []

class Hatchet

attr_reader :id, :level, :animation, :pulses

def initialize(id, level, animation, pulses)
    @id = id
    @level = level
    @animation = Animation.new(animation)
    @pulses = pulses
  end
end

def append_hatchet(hatchet)
  HATCHET[hatchet.id] = hatchet
  HATCHET_ID << hatchet.id
end

# The hatchets
# Credits to ViperSniper for the animations
append_hatchet(Hatchet.new(1351, 1,  879, 8)) # Bronze hatchet
append_hatchet(Hatchet.new(1349, 1,  877, 7)) # Iron hatchet
append_hatchet(Hatchet.new(1353, 1,  875, 6)) # Steel hatchet
append_hatchet(Hatchet.new(1361, 1,  873, 5)) # Black hatchet
append_hatchet(Hatchet.new(1355, 1,  871, 4)) # Mith hatchet
append_hatchet(Hatchet.new(1357, 1,  869, 3)) # Addy hatchet
append_hatchet(Hatchet.new(1359, 1,  867, 2)) # Rune hatchet

HATCHET_ID.reverse!