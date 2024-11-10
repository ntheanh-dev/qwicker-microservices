<#ftl encoding='UTF-8'>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link
            href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap"
            rel="stylesheet"
    />
</head>
<div style="font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2">
    <div style="margin:40px auto;width:70%;padding:20px 0">
        <div style="border-bottom:1px solid #eee">
            <p style="text-align:center">
                <a href="" style="font-size:2.4em;color: #00466a;text-decoration:none;font-weight:600">Mã OTP</a>
            </p>
        </div>
        <p style="font-size:1.1em">Chào ${username}</p>
        <p>Cảm ơn đã đăng ký làm thành viên của Qwicker App. Hãy sử dụng mã OTP dưới đây để tiếp tục quá trình đăng ký thành viên. Mã OTP sẽ hết hạn sau
            <span style="font-weight: 600; color: #1f1f1f;">${otpTTL} phút</span>.
            Đừng chia sẻ mã này cho bất kì ai.
        </p>
        <h2 style="background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;">${otp}</h2>
        <p style="font-size:0.9em;">Trân trọng,<br />Qwicker</p>
        <hr style="border:none;border-top:1px solid #eee" />
    </div>
</div>
</body>
</html>
