package no.sandramoen.hunted.utils

interface GooglePlayServices {
    fun signIn()
    fun signOut()
    fun isSignedIn(): Boolean
    fun getLeaderboard()
    fun fetchHighScore()
    fun getHighScore(): Int
    fun submitScore(score: Int)
    fun rewardAchievement(achievement: String, increment: Int)
    fun showLeaderboard()
    fun showAchievements()
}
