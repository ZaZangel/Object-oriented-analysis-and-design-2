#include "FormWithoutProxy.hpp"
#include <iostream>

FormWithoutProxy::FormWithoutProxy(sf::RenderWindow& wnd, sf::Font& fnt)
    : window(wnd), font(fnt)
{
    initUI();
    initializeDocuments();
}

FormWithoutProxy::~FormWithoutProxy()
{
}

void FormWithoutProxy::initUI()
{
    titleText = std::make_unique<sf::Text>();
    logText = std::make_unique<sf::Text>();

    titleText->setFont(font);
    titleText->setString("WITHOUT PROXY (immediate load)");
    titleText->setCharacterSize(18);
    titleText->setFillColor(sf::Color::White);
    titleText->setPosition(sf::Vector2f(50.0f, 20.0f));

    btnShowInfo.setSize(sf::Vector2f(180.0f, 40.0f));
    btnShowInfo.setPosition(sf::Vector2f(50.0f, 70.0f));
    btnShowInfo.setFillColor(sf::Color::White);
    btnShowInfo.setOutlineThickness(2.0f);
    btnShowInfo.setOutlineColor(sf::Color::Blue);

    btnDisplayFirst.setSize(sf::Vector2f(180.0f, 40.0f));
    btnDisplayFirst.setPosition(sf::Vector2f(250.0f, 70.0f));
    btnDisplayFirst.setFillColor(sf::Color(173, 216, 230));

    btnDisplayAll.setSize(sf::Vector2f(400.0f, 40.0f));
    btnDisplayAll.setPosition(sf::Vector2f(50.0f, 120.0f));
    btnDisplayAll.setFillColor(sf::Color::White);
    btnDisplayAll.setOutlineThickness(2.0f);
    btnDisplayAll.setOutlineColor(sf::Color::Black);

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

void FormWithoutProxy::initializeDocuments()
{
    addToLog("=== INITIALIZATION (without Proxy) ===");
    addToLog("Creating documents directly...");
    addToLog("Warning: this will take time!");
    addToLog("");

    addToLog("Loading document 1...");
    documents.push_back(std::make_shared<RealDocument>("Document_1.pdf", "Report 2024"));

    addToLog("Loading document 2...");
    documents.push_back(std::make_shared<RealDocument>("Document_2.docx", "Contract"));

    addToLog("Loading document 3...");
    documents.push_back(std::make_shared<RealDocument>("Document_3.xlsx", "Financial plan"));

    addToLog("\nCreated " + std::to_string(documents.size()) + " documents");
    addToLog("All documents already loaded to memory!");
    addToLog("================================");
    addToLog("");
}

void FormWithoutProxy::addToLog(const std::string& message)
{
    logContent += message + "\n";
    if (logText)
        logText->setString(logContent);
}

void FormWithoutProxy::handleEvent(const sf::Event& event)
{
    if (event.is<sf::Event::MouseButtonPressed>())
    {
        sf::Vector2i mousePosInt = sf::Mouse::getPosition(window);
        sf::Vector2f mousePos = window.mapPixelToCoords(mousePosInt);

        if (btnClose.getGlobalBounds().contains(mousePos))
        {
            close();
        }
        else if (btnShowInfo.getGlobalBounds().contains(mousePos))
        {
            addToLog("\n=== DOCUMENT INFO ===");
            for (const auto& doc : documents)
            {
                addToLog("File: " + doc->GetFileName());
                addToLog("   Description: " + doc->GetDescription());
                addToLog("   Size: " + std::to_string(doc->GetSize()) + " MB");
                addToLog("");
            }
        }
        else if (btnDisplayFirst.getGlobalBounds().contains(mousePos))
        {
            if (!documents.empty())
            {
                addToLog("\n=== DISPLAY FIRST DOCUMENT ===");
                addToLog("Opening document...");

                documents[0]->Display();

                addToLog("\nDone (fast, already loaded)");
            }
        }
        else if (btnDisplayAll.getGlobalBounds().contains(mousePos))
        {
            addToLog("\n=== DISPLAY ALL DOCUMENTS ===");
            for (auto& doc : documents)
            {
                doc->Display();
            }
            addToLog("\nAll documents displayed");
        }
        else if (btnClearLog.getGlobalBounds().contains(mousePos))
        {
            logContent.clear();
            if (logText)
                logText->setString("");
        }
    }
}

void FormWithoutProxy::draw(sf::RenderWindow& window)
{
    if (!isOpen) return;

    if (titleText)
        window.draw(*titleText);
    window.draw(btnShowInfo);
    window.draw(btnDisplayFirst);
    window.draw(btnDisplayAll);
    window.draw(btnClearLog);
    window.draw(btnClose);
    if (logText)
        window.draw(*logText);
}