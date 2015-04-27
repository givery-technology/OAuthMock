package services.twitter

import twitter4j.TwitterFactory
import twitter4j.Twitter
import twitter4j.auth.RequestToken
import twitter4j.auth.AccessToken


class TwitterManager (key: String, secret: String) {
  private val factory = new TwitterFactory

  def create: Twitter = {
    val twitter = factory.getInstance
    twitter.setOAuthConsumer(key, secret)
    twitter
  }

  def authorizationUri: String = {
    val twitter = create
    twitter.getOAuthRequestToken.getAuthorizationURL
  }

  def authorize(token: String, verifier: String): Twitter = {
    val twitter = create
    val requestToken = new RequestToken(token, secret)
    val accessToken = twitter.getOAuthAccessToken(requestToken, verifier)
    twitter
  }

  def fromAccessToken(token: String, secret: String): Twitter = {
    val twitter = create
    val accessToken = new AccessToken(token, secret)
    twitter.setOAuthAccessToken(accessToken)
    twitter
  }
}

object TwitterManager extends TwitterManager(
  sys.env("TWITTER_CONSUMER_KEY"),
  sys.env("TWITTER_CONSUMER_SECRET")
)
