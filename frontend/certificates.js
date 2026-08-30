async function loadCertificates() {
    const list = document.getElementById("certificates-list");
    const userId = localStorage.getItem("userId");

    if (!userId) {
        list.innerHTML = "<p>Please sign up or log in first.</p>";
        return;
    }

    try {
        const response = await fetch("http://10.121.0.140:8080/api/certificates/user/" + userId);
        const certificates = await response.json();

        if (certificates.length === 0) {
            list.innerHTML = "<p>No certificates yet. Complete a course to earn one!</p>";
            return;
        }

        list.innerHTML = "";
        certificates.forEach(function(cert) {
            const card = document.createElement("div");
            card.className = "course-card";
            card.innerHTML =
                "<h3>" + cert.courseTitle + "</h3>" +
                "<p>Awarded to <strong>" + cert.userName + "</strong></p>" +
                "<p>Issued: " + cert.issueDate + "</p>" +
                "<p style='font-family: IBM Plex Mono, monospace; font-size: 13px; color: var(--moss);'>Certificate Code: " + cert.certificateCode + "</p>" +
                "<button onclick='printCertificate(" +
                    JSON.stringify(cert.courseTitle) + ", " +
                    JSON.stringify(cert.userName) + ", " +
                    JSON.stringify(cert.issueDate) + ", " +
                    JSON.stringify(cert.certificateCode) +
                ")'>Print Certificate</button>";
            list.appendChild(card);
        });
    } catch (error) {
        list.innerHTML = "<p>Could not load certificates. Is the backend running?</p>";
    }
}

function printCertificate(courseTitle, userName, issueDate, certificateCode) {
    const printWindow = window.open("", "_blank");
    printWindow.document.write(
        "<html><head><title>Certificate</title>" +
        "<style>" +
        "body{ font-family: Georgia, serif; text-align:center; padding:80px; border: 12px solid #1B4B3A; margin:20px; }" +
        "h1{ font-size:16px; letter-spacing:3px; text-transform:uppercase; color:#4C7A64; }" +
        "h2{ font-size:42px; color:#0F2E23; margin:20px 0; }" +
        "p{ font-size:18px; color:#333; }" +
        ".course{ font-size:26px; font-weight:bold; margin:24px 0; color:#1B4B3A; }" +
        ".code{ margin-top:60px; font-family:monospace; font-size:13px; color:#888; }" +
        "</style></head><body>" +
        "<h1>Capacity Connect</h1>" +
        "<h2>Certificate of Completion</h2>" +
        "<p>This certifies that</p>" +
        "<div class='course' style='font-size:30px;'>" + userName + "</div>" +
        "<p>has successfully completed</p>" +
        "<div class='course'>" + courseTitle + "</div>" +
        "<p>Issued on " + issueDate + "</p>" +
        "<div class='code'>Certificate Code: " + certificateCode + "</div>" +
        "</body></html>"
    );
    printWindow.document.close();
    printWindow.print();
}

function logout() {
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    localStorage.removeItem("userEmail");
    window.location.href = "index.html";
}

loadCertificates();