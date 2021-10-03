import React,{useState,createContext, useContext,useEffect,useMemo} from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBell,faChevronLeft,faExclamationTriangle,faTrashAlt } from '@fortawesome/free-solid-svg-icons'
import axios from 'axios'
import uniqid from 'uniqid'

const notifContext = createContext();
    
function Circle(){

    const {nbrNotifs,setNotifShown} = useContext(notifContext)

    return (
        <div className="circle" onClick={()=>{setNotifShown(prev=>!prev)}}>
            <FontAwesomeIcon icon={faBell}/>
            <div className="red-circle">
                <p>{nbrNotifs}</p>
            </div>
        </div>
    )
}

function Notif({problem}){

    const [shown, setShown] = useState(false)
    const {setProblemTable} = useContext(notifContext)

    return (
        <>
            <div className="problem" onClick={()=>{setShown(!shown)}}>
                <h3> <FontAwesomeIcon icon={faExclamationTriangle} className="icon"/> {problem.title} </h3>
                <p> {problem.location} </p>
                <div className={"description "+ (shown?"visible":"")}>
                    {problem.description}
                </div>
                <FontAwesomeIcon icon={faChevronLeft} className={"arrow "+(shown?"bottom":"")}/>
                <FontAwesomeIcon 
                    icon={faTrashAlt} 
                    className="remove" 
                    onClick={()=>{setProblemTable(prev=>prev.filter(prob=>prob.id!==problem.id))}}
                />
            </div>
            <hr />
        </>
    )
}

function NotifsContainer(){

    const {notifShown,problemTable} = useContext(notifContext)

    return (
        <div className={"notifications "+(notifShown?"visible":"")}>
            {problemTable.map(problem=>(
                <Notif problem={problem} key={problem.id}/>
            ))}
        </div>
    )
}

export default function Notifications() {
    const [problemTable, setProblemTable] = useState([
        {
            id:1,
            title:"hello",description: "something happened",
            location:"dar"
        }
    ])
    const nbrNotifs = useMemo(()=>problemTable.length,[problemTable])


    const [notifShown, setNotifShown] = useState(false)

    useEffect(()=>{

        const fetchNotif =async ()=>{
            const userData = JSON.parse(localStorage.getItem('userData'));
            await axios.get("http://localhost:8080/Heating/getNews",{params:{
                country:userData.country,
                city:userData.state, 
                email:userData.email
            }}).then(response=>{
                //getting location from localStorage
                const locationString = JSON.parse(localStorage.getItem("userData")).state 
                        + " - " + JSON.parse(localStorage.getItem("userData")).country ;

                response.data.forEach(data=>{
                    const {title,description} = data
                     
                    setProblemTable(prev=>[...prev,{
                        id:uniqid(),
                        title, description,
                        location: locationString
                    }]);
                })
                
            })
        }

        fetchNotif(); 
        setInterval(async()=>{
            await fetchNotif();
        },60000)
    },[])


    const value ={
        notifShown , setNotifShown,
        nbrNotifs, 
        problemTable,setProblemTable
    }

    return (
        <notifContext.Provider value={value}>
            <Circle/>
            <NotifsContainer/>
        </notifContext.Provider>
    )
}
