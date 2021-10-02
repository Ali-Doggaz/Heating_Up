import React,{useEffect, useMemo, useState,useContext, createContext} from 'react'
import countriesUtility from '../utilities/countriesUtility'
import { useHistory } from "react-router-dom";
import axios from 'axios';

const countries= countriesUtility.getCountries();
const formContext = createContext();

function FormInput({name}){

    const {formInputs, setFormInputs}= useContext(formContext)

    const handleChange=(e)=>{
        const {value} = e.target;
        setFormInputs(prev=>({
            ...prev,
            [name] : value
        }))
    }

    return (
        <div className="input">
            <label htmlFor={name}>{name}:</label>
            <div className="input-space">
                <input type="email" 
                    id={name} 
                    placeholder={name} 
                    name={name}
                    value={formInputs[name]} 
                    onChange={handleChange} 
                />
                <div className="line"></div>
            </div>
        </div>
    )
}

function SelectInput({name,options}){
    const {formInputs, setFormInputs}= useContext(formContext)
    
    const handleChange=(e)=>{
        const {value} = e.target;
        setFormInputs(prev=>({
            ...prev,
            [name] : value
        }))
    }

    return (
        <div className="input">
            <label htmlFor={name}>{name}:</label>
            <div className="input-space">
                <select 
                    placeholder={name}
                    name={name} 
                    value={formInputs[name]}
                    onChange={handleChange}
                    id=""
                >
                    {options.map((option,index)=>(
                        <option value={option.country||option} key={index}>
                            {option.country||option}
                        </option>
                    ))}
                </select>
                <div className="line"></div>
            </div>
        </div>
    )
}

export default function Form() {

    const history= useHistory();

    const [loadingSubmit, setLoadingSubmit] = useState(false)
    const [formErr, setFormErr] = useState("")

    const [formInputs, setFormInputs] = useState({
        email:"",
        country: "Tunisia",
        state:"Ariana",
    })

    const states= useMemo(()=>{
        return countriesUtility.getStates(formInputs.country)
    },[formInputs.country])

    useEffect(()=>{
        setFormInputs(prev=>({
            ...prev, 
            state:states[0]?states[0]:'none'
        }))
    },[states])

    useEffect(()=>{
        const userDataJson= localStorage.getItem('userData');
        if(!userDataJson) return ; 
        const userData= JSON.parse(userDataJson);
        setFormInputs(userData)
    },[])


    const onSubmit =async (e)=>{
        e.preventDefault();

        if(!formInputs.email.trim()){
            setFormErr("please specify email")
            return ;
        }

        setLoadingSubmit(true);

        //post to server here
        await axios.post("http://localhost:8080/Heating/user",{
            "email": formInputs.email,
            "city": formInputs.state,
            "country": formInputs.country
        }).catch(error=>{
            setFormErr("something went wrong try again later!");
            console.log(error)
        })
        

        //add to localStorage
        localStorage.setItem('userData',JSON.stringify(formInputs))


        setLoadingSubmit(false);


        return history.push("/home");
    }

    const value={
        formInputs, setFormInputs
    }
    return (
        <formContext.Provider value={value}>
            <div className="country-form">
                <form>
                    <div className="inputs">
                        <FormInput name="email" />
                        <SelectInput name="country" options={countries} />
                        <SelectInput name="state" options={states}/>
                    </div>
                    <button 
                        className="submit-button" 
                        onClick={onSubmit}
                        disabled={loadingSubmit}
                    > {loadingSubmit?'...':'Join'} </button>
                    {formErr? (
                        <p className="error">{formErr}</p>
                    ):''}
                </form>
            </div>
        </formContext.Provider>
    )
}
