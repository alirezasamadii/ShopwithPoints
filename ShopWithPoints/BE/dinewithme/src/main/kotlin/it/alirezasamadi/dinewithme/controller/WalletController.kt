package it.alirezasamadi.dinewithme.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.zxing.WriterException
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.exceptions.HttpStatusException
import it.alirezasamadi.dinewithme.model.Wallet
import it.alirezasamadi.dinewithme.service.WalletService
import org.lome.niceqr.QrConfiguration
import org.lome.niceqr.QrEngine
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*
import javax.imageio.ImageIO


@Controller
open class WalletController(val walletService: WalletService) {

    @Get("/api/wallet/users/{userId}")
    fun getWalletByUser(@PathVariable userId: String): Wallet {
       return walletService.findWalletByUserId(userId) ?: throw HttpStatusException(HttpStatus.NOT_FOUND, "")
    }

    @Get(value = "/api/wallet/users/{userId}/qr", produces = [MediaType.IMAGE_PNG])
    fun getRedeemingQrCode(@PathVariable userId: String): ByteArray {
         val wallet = walletService.findWalletByUserId(userId) ?: throw HttpStatusException(HttpStatus.NOT_FOUND, "")

        val expiryDate = LocalDateTime.now().plusMinutes(1)
        val millis = expiryDate.toInstant(OffsetDateTime.now().offset).toEpochMilli()

        val token: String = JWT.create()
            .withIssuer("dinewithme")
            .withExpiresAt(Date(millis))
            .withPayload(
                mapOf(
                    "walletId" to wallet._id,
                    "userId" to userId,
                    "points" to wallet.points
                )
            )
            .sign(Algorithm.HMAC256("dinewithmeSecret"))


        return generateQRCodeImage(token, 250,250)
    }

    @Get(value = "/api/wallet/users/{userId}/token")
    fun getRedeemingQrCodeToken(@PathVariable userId: String): Any {
        val wallet = walletService.findWalletByUserId(userId) ?: throw HttpStatusException(HttpStatus.NOT_FOUND, "")

        val expiryDate = LocalDateTime.now().plusMinutes(1)
        val millis = expiryDate.toInstant(OffsetDateTime.now().offset).toEpochMilli()

        val token: String = JWT.create()
            .withIssuer("dinewithme")
            .withExpiresAt(Date(millis))
            .withPayload(
                mapOf(
                    "walletId" to wallet._id,
                    "userId" to userId,
                    "points" to wallet.points
                )
            )
            .sign(Algorithm.HMAC256("dinewithmeSecret"))


        return object {
            var token = token
        }
    }


    @Post("/api/redeem")
    @Status(HttpStatus.NO_CONTENT)
    fun redeemPoints(@QueryValue token: String, @QueryValue points: Int) {
        val claims = JWT.decode(token).claims
        walletService.decreasePoints(claims["userId"]?.asString() ?: "",points)
    }

    @Throws(WriterException::class, IOException::class)
    fun generateQRCodeImage(text: String?, width: Int, height: Int): ByteArray {
        var qr = QrEngine.buildQrCode(
            text,
            QrConfiguration.builder()
                .withSize(200)
                .withRelativeBorderSize(.05)
                .withRelativeBorderRound(.2)
                .withDarkColor(Color(0x0063, 0x000B, 0x00A5))
                .withLightColor(Color.white)
                .withPositionalsColor(Color(0x00F4, 0x0014, 0x0038))
                .withCircularPositionals(true)
                .build()
        )


        val baos = ByteArrayOutputStream()
        ImageIO.write(qr, "png", baos)
        return baos.toByteArray()
    }

}
