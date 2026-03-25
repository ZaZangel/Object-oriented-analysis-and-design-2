#include "FormWithProxy.hpp"
#include <iostream>

FormWithProxy::FormWithProxy(sf::RenderWindow& wnd, sf::Font& fnt)
    : window(wnd), font(fnt)
{
    initUI();
    initializeDocuments();
}

FormWithProxy::~FormWithProxy()
{
}

void FormWithProxy::initUI()
{
    titleText = std::make_unique<sf::Text>();
    logText = std::make_unique<sf::Text>();

    titleText->setFont(font);
    titleText->setString("WITH PROXY PATTERN (lazy loading)");
    titleText->setCharacterSize(18);
    titleText->setFillColor(sf::Color::White);
    titleText->setPosition(sf::Vector2f(50.0f, 20.0f));

    btnShowPreview.setSize(sf::Vector2f(180.0f, 40.0f));
    btnShowPreview.setPosition(sf::Vector2f(50.0f, 70.0f));
    btnShowPreview.setFillColor(sf::Color::White);
    btnShowPreview.setOutlineThickness(2.0f);
    btnShowPreview.setOutlineColor(sf::Color::Blue);

    btnLoadFirst.setSize(sf::Vector2f(180.0f, 40.0f));
    btnLoadFirst.setPosition(sf::Vector2f(250.0f, 70.0f));
    btnLoadFirst.setFillColor(sf::Color(173, 216, 230));

    btnLoadAll.setSize(sf::Vector2f(180.0f, 40.0f));
    btnLoadAll.setPosition(sf::Vector2f(50.0f, 120.0f));
    btnLoadAll.setFillColor(sf::Color::White);
    btnLoadAll.setOutlineThickness(2.0f);
    btnLoadAll.setOutlineColor(sf::Color::Black);

    btnRepeatAccess.setSize(sf::Vector2f(180.0f, 40.0f));
    btnRepeatAccess.setPosition(sf::Vector2f(250.0f, 120.0f));
    btnRepeatAccess.setFillColor(sf::Color(255, 255, 224));

    btnClearLog.setSize(sf::Vector2f(400.0f, 40.0f));
    btnClearLog.setPosition(sf::Vector2f(50.0f, 170.0f));
    btnClearLog.setFillColor(sf::Color(240, 128, 128));

    btnClose.setSize(sf::Vector2f(100.0f, 30.0f));
    btnClose.setPosition(sf::Vector2f(650.0f, 20.0f));
    btnClose.setFillColor(sf::Color::Red);

    logText->setFont(font);
    logText->setCharacterSize(12);
    logText->setFillColor(sf::Color::Yellow);
    logText->setPosition(sf::Vector2f(50.0f, 230.0f));
}

void FormWithProxy::initializeDocuments()
{
    addToLog("=== INITIALIZATION (with Proxy) ===");
    addToLog("Creating documents through PROXY...");

    documents.push_back(std::make_shared<ProxyDocument>("Document_1.pdf", "Report 2024"));
    documents.push_back(std::make_shared<ProxyDocument>("Document_2.docx", "Contract"));
    documents.push_back(std::make_shared<ProxyDocument>("Document_3.xlsx", "Financial plan"));

    addToLog("Created " + std::to_string(documents.size()) + " documents");
    addToLog("Real documents NOT loaded yet!");
    addToLog("================================");
    addToLog("");
}

void FormWithProxy::addToLog(const std::string& message)
{
    logContent += message + "\n";
    if (logText)
        logText->setString(logContent);
}

void FormWithProxy::handleEvent(const sf::Event& event)
{
    if (event.is<sf::Event::MouseButtonPressed>())
    {
        sf::Vector2i mousePosInt = sf::Mouse::getPosition(window);
        sf::Vector2f mousePos = window.mapPixelToCoords(mousePosInt);

        if (btnClose.getGlobalBounds().contains(mousePos))
        {
            close();
        }
        else if (btnShowPreview.getGlobalBounds().contains(mousePos))
        {
            addToLog("\n=== DOCUMENT PREVIEW ===");
            for (const auto& doc : documents)
            {
                auto* proxy = dynamic_cast<ProxyDocument*>(doc.get());
                if (proxy)
                {
                    addToLog("File: " + proxy->GetFileName());
                    std::string status = proxy->IsLoaded() ? "Loaded" : "Not loaded";
                    addToLog("   Status: " + status);
                    addToLog("   Access count: " + std::to_string(proxy->GetAccessCount()));
                    addToLog("");
                }
            }
        }
        else if (btnLoadFirst.getGlobalBounds().contains(mousePos))
        {
            if (!documents.empty())
            {
                addToLog("\n=== LOAD FIRST DOCUMENT ===");
                addToLog("Requesting display...");

                documents[0]->Display();

                auto* proxy = dynamic_cast<ProxyDocument*>(documents[0].get());
                if (proxy)
                    addToLog("\n" + proxy->GetStatistics());
            }
        }
        else if (btnLoadAll.getGlobalBounds().contains(mousePos))
        {
            addToLog("\n=== LOAD ALL DOCUMENTS ===");
            for (auto& doc : documents)
                doc->Load();
            addToLog("\nAll documents loaded");
        }
        else if (btnRepeatAccess.getGlobalBounds().contains(mousePos))
        {
            if (!documents.empty())
            {
                addToLog("\n=== REPEAT ACCESS ===");
                addToLog("Requesting same document again...");

                documents[0]->Display();

                auto* proxy = dynamic_cast<ProxyDocument*>(documents[0].get());
                if (proxy)
                    addToLog("\n" + proxy->GetStatistics());
            }
        }
        else if (btnClearLog.getGlobalBounds().contains(mousePos))
        {
            logContent.clear();
            if (logText)
                logText->setString("");
        }
    }
}

void FormWithProxy::draw(sf::RenderWindow& window)
{
    if (!isOpen) return;

    if (titleText)
        window.draw(*titleText);
    window.draw(btnShowPreview);
    window.draw(btnLoadFirst);
    window.draw(btnLoadAll);
    window.draw(btnRepeatAccess);
    window.draw(btnClearLog);
    window.draw(btnClose);
    if (logText)
        window.draw(*logText);
}