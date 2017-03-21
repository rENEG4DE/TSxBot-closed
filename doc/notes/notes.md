#Notes
##Issues
- Permission-problem bei absetzen von Shell-Kommando   
- Performance Probleme von Plugin (AfkMover) beheben   
- Fix .lck log-file-issue

##Development notes
###Global
- ~~Alle Sammlungen von Werten, enums, maps, etc.. bekommen das prefix lib~~ __erledigt__

###Database
- ~~Opt-werte unabhängig von geschriebenen enities speichern~~ __abgelehnt__

####Entity
- Entity-Decorator (Für DbID u.ä.)
- Kollektoren für diverse member von Entities verfügbar machen 
- Entity persistenz management

###System
- ~~Globales Answer-Request-System~~
- Eine Schnittstelle für System.Out, Log-out und Channel Out
- Exception handling

####Config
- Entity-Referenz-Problem lösen
	- Vielleicht eine Funktion die anhand eines Entitynamen die absolute Referenz zurückgeben kann ?

###Plugin
- ChannelADM *Feature*
	- Im Game-channel joinende User sollen in Subchannel verschoben werden
	- Joinen 2 User innerhalb eines Intervalls werden diese in den gleichen Channel geschoben
	- Konfigurierbar ? User werden entweder balanced oder immer in neue Subchannel geschoben
	- Teams sollen absteigend nach größe sortiert werden
	
###Unspecified
- HL-Logging System via Decorator

##Info
- Konfigurierbar über Servergruppen

##Coding notes
- Kombination aus Decorator und Chain of Responsibility
- Von einem Subsystem gesharedten code in mediator

##Future notes
- Dem Bot das Sprechen wieder beibringen
- Channel für Reports vom TSxBot
	- BotChannel ermöglichen

##Release roadmap
- Copyprotection

##Administrative
