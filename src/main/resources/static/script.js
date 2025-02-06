document.addEventListener("DOMContentLoaded", function () {
    // 1. Zobrazenie uvítacieho hlásenia
    alert("Vitajte v Knižničnom systéme!");

    // 2. Zmena textu v info sekcii po 3 sekundách
    setTimeout(() => {
        document.querySelector(".info-section p").textContent =
            "Ak ešte nemáte účet, registrujte sa!";
    }, 3000);

    // 3. Dynamická zmena farby hlavičky po kliknutí
    document.querySelector(".header").addEventListener("click", function () {
        this.style.backgroundColor = this.style.backgroundColor === "red" ? "#007bbf" : "red";
    });

    // 4. Uloženie informácie, či používateľ už stránku navštívil
    if (!localStorage.getItem("visited")) {
        localStorage.setItem("visited", "true");
        alert("Prvýkrát na stránke? Vitajte!");
    } else {
        console.log("Vitajte späť!");
    }

    // 5. Pridanie nového odkazu do navigácie dynamicky
    const newNavItem = document.createElement("li");
    newNavItem.innerHTML = '<a href="/kontakt">Kontakt</a>';
    document.querySelector(".nav-links").appendChild(newNavItem);

    // 6. Efekt pri prechode myšou cez odkazy
    document.querySelectorAll(".nav-links a").forEach(link => {
        link.addEventListener("mouseover", () => link.style.color = "yellow");
        link.addEventListener("mouseout", () => link.style.color = "white");
    });

    // 7. Animácia postupného zobrazovania textu v info sekcii
    const infoText = document.querySelector(".info-section p");
    infoText.style.opacity = "0";
    let opacity = 0;
    const fadeIn = setInterval(() => {
        if (opacity < 1) {
            opacity += 0.1;
            infoText.style.opacity = opacity;
        } else {
            clearInterval(fadeIn);
        }
    }, 100);

    // 8. Po kliknutí na tlačidlo sa zobrazí aktuálny čas
    const timeButton = document.createElement("button");
    timeButton.textContent = "Zisti aktuálny čas";
    timeButton.style.marginTop = "10px";
    timeButton.onclick = () => alert("Aktuálny čas: " + new Date().toLocaleTimeString());
    document.body.appendChild(timeButton);
});
