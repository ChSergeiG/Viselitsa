package ru.chsergeig.bot.viselitsa

class RandomWordProviderHolder private constructor() {

    companion object {
        var provider = RandomWordProvider.Provider.CASTLOTS
    }

}
