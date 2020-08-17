#!/usr/bin/python3

import random
import sys, tty, termios
import os

def clear():
    os.system('clear')

def getch():
    fd = sys.stdin.fileno()
    old_settings = termios.tcgetattr(fd)
    try:
        tty.setraw(fd)
        ch = sys.stdin.read(1)
    finally:
        termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
    return ch

class Card:
    spade, club, heart, diamond = u'\u2660', u'\u2663', u'\u2665', u'\u2666'
    #spade, club, heart, diamond = '\u2664', '\u2667', '\u2661', '\u2662'
    ranks = ('A', '2', '3', '4', '5', '6', '7', '8', '9', '10', 'J', 'Q', 'K')
    suits = (spade, club, diamond, heart)

    def __init__(self, rank, suit):
        self.rank = rank
        self.suit = suit
        if self.is_ace():
            self.value = 11
        else:
            self.value = min(Card.ranks.index(rank) + 1,  10)

    def __repr__(self):
        return "{}{}".format(self.rank, self.suit)

    def is_ace(self):
        return self.rank == 'A'

class CardSource:
    def __init__(self, shuffle = True, num_decks = 1):
        self.cards = []
        
        for _ in range(num_decks):
            for suit in Card.suits:
                for rank in Card.ranks:
                    self.cards.append(Card(rank, suit))
        
        if (shuffle):
            random.shuffle(self.cards)

    def next(self):
        return self.cards.pop()
        

class Hand:
    target = 21

    def __init__(self, name, source):
        self.cards = []
        self.ace_count = 0
        self.source = source
        self.name = name
        self.total = 0

    def hit(self):
        next_card = self.source.next()
        self.cards.append(next_card)
        if next_card.is_ace():
            self.ace_count += 1

    def value(self):
        values = []
        for card in self.cards:
            values.append(card.value)
        total = sum(values)
        for _ in range(self.ace_count):
            if total > Hand.target:
                total -= 10
        return total

    def is_twenty_one(self):
        return self.value() == Hand.target

    def is_blackjack(self):
        return  self.is_twenty_one() and \
            len(self.cards) == 2

    def is_busted(self):
        return self.value() > Hand.target

    def beats(self, other):
        loses = self.is_busted()
        if not loses:
            loses = not other.is_busted() \
                and other.value() > self.value()
        
        return not loses

    def outcome(self, other):
        if self.is_busted():
            return Game.dealer_wins

        if self.is_blackjack():
            return Game.player_blackjack

        if other.is_busted():
            return Game.player_wins
        
        if other.value() == self.value():
            return Game.draw
        elif other.value() > self.value():
            return Game.dealer_wins
        else:
            return Game.player_wins

    def __repr__(self):
        return "{} - {}\nTot: {}".format(self.cards, self.name, self.value())

class Outcome:
    def __init__(self, msg, pot_adjuster):
        self.msg = msg.format("Go again? [y/n]: ")
        self.pot_adjuster = pot_adjuster

class Game:
    player_wins = Outcome("Player wins! {}", lambda bet : bet*2)
    dealer_wins = Outcome("Player loses! {}", lambda bet : 0)
    draw = Outcome("Draw! {}", lambda bet : bet)
    player_blackjack = Outcome("** Blackjack ! ** {}", lambda bet : bet*2.5)
    
    def __init__(self, pot, bet):
        self.pot = pot - bet
        self.orig_bet = bet
        self.bet = bet
        self.shoe = None
        self.new_shoe()
        self.new_hands()

    def new_hands(self):
        self.player_hand = Hand("Player", self.shoe)
        self.dealer_hand = Hand("Dealer", self.shoe)

    def play_loop(self):
        while(True):
            outcome = self.play()
            if self.ask_play_again(outcome):
                self.new_hands()
                self.bet = self.orig_bet
                self.pot -= self.bet
                self.new_shoe()
            else:
                break

    def play(self):
        clear()

        self.set_up_hands()
        self.play_player_hand()
        self.play_dealer_hand()

        outcome = self.player_hand.outcome(self.dealer_hand)
        self.pot += outcome.pot_adjuster(self.bet)
        self.show_info()
        return outcome

    def play_player_hand(self):
        more_hits = True
        if not self.player_hand.is_twenty_one():
            print("Hit? Double Down? [y/n/d]")
            choice = getch()
            if choice == 'y':
                self.hit(self.player_hand)
                if self.player_hand.is_busted():
                    more_hits = False
            elif choice == 'd':
                more_hits = False
                self.pot -= self.bet
                self.bet *= 2
                self.hit(self.player_hand)
            else:
                more_hits = False

        if more_hits: 
            while(not self.player_hand.is_twenty_one()):
                print("Hit? [y/n]")
                choice = getch()
                if choice == 'y':
                    self.hit(self.player_hand)
                    if self.player_hand.is_busted():
                        break
                else:
                    break
    
    def play_dealer_hand(self):
        while self.dealer_hand.value() < 17:
            self.hit(self.dealer_hand)
            if self.dealer_hand.is_busted():
                break

    def set_up_hands(self):
        self.player_hand.hit()
        self.dealer_hand.hit()
        self.hit(self.player_hand)

    def hit(self, hand):
        hand.hit()
        self.show_info()
        
    def show_info(self):
        clear()
        print("\n{}\n\n{}\n".format(self.dealer_hand, self.player_hand))
        print("Pot: ${}  -  Bet: ${}".format(self.pot, self.bet))
        print("Cards Remaining: {}\n".format(len(self.shoe.cards)))

    def ask_play_again(self, outcome):
        print(outcome.msg)
        return getch() == 'y'

    def new_shoe(self):
        if self.shoe == None or \
            len(self.shoe.cards) < 20:
            self.shoe = CardSource(num_decks=7)

game = Game(100, 10)
game.play_loop()

