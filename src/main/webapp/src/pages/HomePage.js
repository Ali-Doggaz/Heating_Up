import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCog } from '@fortawesome/free-solid-svg-icons'
import earthImage from '../assets/images/earth-heating.jpg'
import Notifications from '../components/Notifications'
import usDroughtImg from '../assets/images/us-drought-monitor.jpg'
import africaWarming from '../assets/images/africa-warming.jpg'
import wildFireImg from '../assets/images/wildfire.jpg'


const top_news =[
    {
        image: usDroughtImg,
        title:'US Drought Monitor',
        location:'United states',
        description:`The Drought Monitor focuses on broad-scale conditions.
        Local conditions may vary. For more information on the
        Drought Monitor, go to https://droughtmonitor.unl.edu/About.aspx`
    },
    {
        image: africaWarming,
        title:'Africa And the Climate Crisis',
        location:'Africa',
        description:`The African continent will be hit the hardest by climate change, despite contributing only 4% to global carbon emissions. This vulnerability is driven by high levels of poverty across the continent leaving many without the resources to buffer themselves and recover from the changing climate.`
    },
    {
        image: wildFireImg,
        title:'2021 North American Wildfire Season',
        location:'Canada and the United states',
        description:'Both countries are unable to share resources as they fight multiple wildfires within their respective borders.'
    },
]

export default function HomePage() {

    const [showNav, setShowNav] = useState(false);

    useEffect(()=>{
        const news = document.querySelectorAll(".new");
        console.log(news)

        var options = {
            root: document.querySelector('#scrollArea'),
            rootMargin: '0px 0px -100px 0px',
            threshold: 1
        }
          
        var observer = new IntersectionObserver(function(entries,observer){
            entries.forEach(entry=>{
                if(!entry.isIntersecting){
                    return;
                }   
                entry.target.classList.add("visible");
            })
        }, options);

        news.forEach(neww=>{
            observer.observe(neww)
        })

        return ()=>{
            observer.disconnect();
        }
    },[])

    return (
        <div className="home">
            <div className="settings" >
                <FontAwesomeIcon 
                    icon={faCog} 
                    className={"settings-icon "+(showNav?"rotate-180":"")}
                    onClick={()=>{setShowNav(!showNav)}}    
                />
                <Link to="/" className={showNav?"visible":""}>Change Settings</Link>
            </div>
            <div className="top-news container">
                <h3>DAILY NEWS <Link to="/search">Search(soon)</Link></h3>
                
                <div className="news-container">
                    {
                        top_news.map((top_new,index)=>(
                            <div className={"new "+((index%2)?"inverse":"")} key={index}>
                                <img src={top_new.image} alt="" />
                                <div className="content">
                                    <h3> {top_new.title.toUpperCase()} </h3>
                                    <p className="location"> {top_new.location} </p>
                                    <p className="description"> {top_new.description} </p>
                                </div>
                            </div>
                        ))
                    }
                </div>
            </div>
            <Notifications/>
        </div>
    )
}
