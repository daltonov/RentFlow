const propertiesList = document.getElementById("properties-list");
const propertiesStatus = document.getElementById("properties-status");

const moneyFormat = new Intl.NumberFormat("ru-RU", {
    style: "currency",
    currency: "RUB"
});

async function loadProperties() {
    propertiesStatus.textContent = "Загружаем квартиры…";
    propertiesList.replaceChildren();

    try {
        const response = await fetch("/api/properties");

        if (!response.ok) {
            throw new Error(`Ошибка API: HTTP ${response.status}`);
        }

        const properties = await response.json();

        if (properties.length === 0) {
            propertiesStatus.textContent = "Квартир пока нет.";
            return;
        }

        for (const property of properties) {
            const item = document.createElement("li");

            item.textContent =
                `${property.name} — ${property.address}. ` +
                `До ${property.maxGuests} гостей. ` +
                `${moneyFormat.format(property.defaultPrice)} за сутки.`;

            propertiesList.append(item);
        }

        propertiesStatus.textContent = "";
    } catch (error) {
        propertiesStatus.textContent =
                "Не удалось загрузить квартиры. Попробуйте обновить страницу.";

        console.error(error);
    }
}

loadProperties();