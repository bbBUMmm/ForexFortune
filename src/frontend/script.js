document.addEventListener("DOMContentLoaded", function() {
    const apiUrl = 'http://localhost:8080/allForexStocks';
    const fetchButton = document.getElementById('fetchButton');
    const currencyDisplay = document.getElementById('currencyDisplay');
    const analyzeButton = document.getElementById('analyzeButton');
    const analysisResult = document.getElementById('analysisResult');

    let currencyPairs = [];

    // Getting the list of currency pairs
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            currencyPairs = data;
            displayCurrencyPairs(currencyPairs);
        })
        .catch(error => console.error("Error fetching currency pairs:", error));

    fetchButton.addEventListener('click', () => {
        if (currencyPairs.length > 0) {
            const randomCurrency = currencyPairs[Math.floor(Math.random() * currencyPairs.length)];
            currencyDisplay.textContent = `Random Currency Pair: ${randomCurrency.name} (${randomCurrency.symbol})`;
            analyzeButton.style.display = "block";
            analysisResult.style.display = "none";
            analyzeButton.disabled = false;
        }
    });


    // Random currency pair analysis
    analyzeButton.addEventListener('click', () => {
        const isBuy = Math.random() > 0.5; // Случайно выбираем BUY или SELL
        analysisResult.textContent = isBuy ? "BUY IT" : "SELL IT";
        analysisResult.className = `analysis-result ${isBuy ? "buy" : "sell"}`;
        analysisResult.style.display = "block";
        analyzeButton.disabled = true;
    });


    // Function for displaying all currency pairs
    function displayCurrencyPairs(data) {
        const currencyPairsContainer = document.getElementById("currency-pairs");
        data.forEach(currency => {
            const currencyElement = document.createElement("div");
            currencyElement.className = "currency";
            currencyElement.innerHTML = `<h2>${currency.name}</h2><p>Symbol: ${currency.symbol}</p>`;
            currencyPairsContainer.appendChild(currencyElement);
        });
    }
});
